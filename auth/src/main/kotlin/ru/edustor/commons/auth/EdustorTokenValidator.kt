package ru.edustor.commons.auth

import io.jsonwebtoken.Jwts
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import ru.edustor.commons.auth.model.EdustorAuthProfile
import java.security.PublicKey
import java.security.Security
import java.util.*

@Component
open class EdustorTokenValidator(environment: Environment) {

    val publicKey: PublicKey

    init {
        val pemKeyBase64 = environment.getRequiredProperty("edustor.auth.jwk-pub-key")
        val pemKey = Base64.getDecoder().decode(pemKeyBase64)

        val result = PEMParser(pemKey.inputStream().reader()).readObject()
        val pemObject = result as? SubjectPublicKeyInfo
                ?: throw IllegalStateException("edustor.auth.jwk-pub-key must be base64-encoded public PEM file")

        val converter = JcaPEMKeyConverter().setProvider("BC")
        Security.addProvider(BouncyCastleProvider())
        publicKey = converter.getPublicKey(pemObject)
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