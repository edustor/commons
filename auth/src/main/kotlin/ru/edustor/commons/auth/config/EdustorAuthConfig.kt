package ru.edustor.commons.auth.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import ru.edustor.commons.auth.EdustorTokenValidator
import ru.edustor.commons.auth.internal.EdustorAuthProfileResolver
import ru.edustor.commons.auth.internal.ScopeValidationInterceptor

@Configuration
@ConditionalOnWebApplication
open class EdustorAuthConfig(val validator: EdustorTokenValidator,
                             val scopeVaInterceptorRegistry: ScopeValidationInterceptor) : WebMvcConfigurerAdapter() {
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        val resolver = EdustorAuthProfileResolver(validator)
        argumentResolvers.add(resolver)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(scopeVaInterceptorRegistry)
    }
}