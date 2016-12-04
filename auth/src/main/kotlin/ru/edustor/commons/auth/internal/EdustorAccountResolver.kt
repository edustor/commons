package ru.edustor.commons.auth.internal

import io.jsonwebtoken.JwtException
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import ru.edustor.commons.auth.EdustorTokenValidator
import ru.edustor.commons.auth.exception.UnauthorizedException
import ru.edustor.commons.protobuf.proto.internal.EdustorAccountsProtos.EdustorAccount

@Component
open class EdustorAccountResolver(val validator: EdustorTokenValidator) : HandlerMethodArgumentResolver {
    override fun resolveArgument(parameter: MethodParameter?, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        try {
            val tokenStr = webRequest.getHeader("Authorization")
                    ?: throw UnauthorizedException("Authorization header is not provided")
            val account = validator.validate(tokenStr)
            return account
        } catch (e: JwtException) {
            throw UnauthorizedException("Provided token is invalid")
        }
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val result = parameter.parameterType == EdustorAccount::class.java
        return result
    }
}