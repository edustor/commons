package ru.edustor.commons.auth

import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import ru.edustor.commons.protobuf.proto.internal.EdustorAccountsProtos.EdustorAccount
import ru.edustor.commons.protobuf.proto.internal.EdustorAccountsProtos.EdustorToken
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

@Component
open class EdustorTokenValidator {

    val publicKey: PublicKey

    init {
        val keyBytes = javaClass.classLoader.getResourceAsStream("jwk.pub.der").readBytes()
        val spec = X509EncodedKeySpec(keyBytes)
        publicKey = KeyFactory.getInstance("RSA").generatePublic(spec)
    }

    fun validate(token: String) : EdustorAccount {
        val parsedToken = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .body



        val scopeStr = parsedToken["scope"] as String
        val scope = scopeStr.split(",")

        val edustorToken = EdustorToken.newBuilder()
                .addAllScope(scope)
                .setRaw(token)
                .build()

        val account = EdustorAccount.newBuilder()
                .setUuid(parsedToken.subject)
                .setActiveToken(edustorToken)
                .build()

        return account
    }
}