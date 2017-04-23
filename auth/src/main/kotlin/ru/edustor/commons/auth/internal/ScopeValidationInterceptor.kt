package ru.edustor.commons.auth.internal

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import ru.edustor.commons.auth.annotation.RequiresAuth
import ru.edustor.commons.auth.exception.ForbiddenException
import ru.edustor.commons.auth.exception.UnauthorizedException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
open class ScopeValidationInterceptor(val edustorAuthProfileResolver: EdustorAuthProfileResolver) : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse?, handler: Any): Boolean {
        if (handler !is HandlerMethod || !handler.hasMethodAnnotation(RequiresAuth::class.java)) {
            return true
        }

        val scopeStr = handler.getMethodAnnotation(RequiresAuth::class.java).scope

        val requiredScopesVariants = scopeStr.split(" | ")
                .map { it.split(" ").filter(String::isNotEmpty) }

        val tokenStr = request.getHeader("Authorization")
                ?: throw UnauthorizedException("Authorization header is not provided")

        val authProfile = edustorAuthProfileResolver.getByAuthToken(tokenStr)

        val authorized = requiredScopesVariants.any { it.subtract(authProfile.scope).isEmpty() }
        if (!authorized) {
            throw ForbiddenException("Your token doesn't permit this action")
        }

        return true
    }
}