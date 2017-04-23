package ru.edustor.commons.auth

import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import ru.edustor.commons.auth.model.EdustorAuthProfile
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

    fun validate(token: String): EdustorAuthProfile {
        val parsedToken = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .body

        val scopeStr = parsedToken["scope"] as String
        val scope = scopeStr.split(" ")

        val account = EdustorAuthProfile(accountId = parsedToken.subject,
                scope = scope,
                rawToken = token)

        return account
    }
}