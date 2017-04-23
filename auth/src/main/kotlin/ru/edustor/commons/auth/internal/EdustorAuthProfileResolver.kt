package ru.edustor.commons.auth.internal

import io.jsonwebtoken.JwtException
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import ru.edustor.commons.auth.EdustorTokenValidator
import ru.edustor.commons.auth.exception.UnauthorizedException
import ru.edustor.commons.auth.model.EdustorAuthProfile

open class EdustorAuthProfileResolver(val validator: EdustorTokenValidator) : HandlerMethodArgumentResolver {
    override fun resolveArgument(parameter: MethodParameter?, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        val tokenStr = webRequest.getHeader("Authorization")
                ?: throw UnauthorizedException("Authorization header is not provided")

        return getByAuthToken(tokenStr)
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val result = parameter.parameterType == EdustorAuthProfile::class.java
        return result
    }

    fun getByAuthToken(authToken: String): EdustorAuthProfile {
        try {
            val account = validator.validate(authToken)
            return account
        } catch (e: JwtException) {
            throw UnauthorizedException("Provided token is invalid")
        }
    }
}