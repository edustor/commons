package ru.edustor.commons.auth.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import ru.edustor.commons.auth.internal.EdustorAccountResolver

@Configuration
@ConditionalOnWebApplication
open class ArgumentResolverConfig(val resolver: EdustorAccountResolver) : WebMvcConfigurerAdapter() {
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(resolver)
    }
}