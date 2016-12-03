package ru.edustor.commons.protobuf.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter

@Configuration
@ConditionalOnWebApplication
open class ProtobufConfig {
    @Bean
    open fun protobufMessageConverter(): ProtobufHttpMessageConverter {
        return ProtobufHttpMessageConverter()
    }
}