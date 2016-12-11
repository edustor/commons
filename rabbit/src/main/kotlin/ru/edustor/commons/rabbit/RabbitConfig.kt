package ru.edustor.commons.rabbit

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.interceptor.RetryOperationsInterceptor

@Suppress("SpringKotlinAutowiring")
@Configuration
@ConditionalOnClass(EnableRabbit::class)
open class RabbitConfig {
    @Bean
    open fun rabbitRejectedExchange(): TopicExchange {
        return TopicExchange("reject.edustor", true, false)
    }

    @Bean
    open fun rabbitRejectedQueue(): Queue {
        return Queue("rejected.edustor", true, false, false)
    }

    @Bean
    open fun rabbitRejectedBinding(): Binding {
        return Binding("rejected.edustor", Binding.DestinationType.QUEUE, "reject.edustor", "#", null)
    }

    @Bean
    open fun jacksonMessageConverter(): Jackson2JsonMessageConverter {
        val converter = Jackson2JsonMessageConverter()
        return converter
    }

    @Autowired fun configureContainer(factory: SimpleRabbitListenerContainerFactory) {
        factory.setAdviceChain(
                interceptor()
        )
        factory.setMessageConverter(jacksonMessageConverter())
    }

    @Autowired
    fun configureRabbitTemplate(template: RabbitTemplate) {
        template.messageConverter = jacksonMessageConverter()
    }

    @Bean
    open fun interceptor(): RetryOperationsInterceptor {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 5.0, 10000)
                .recoverer(RejectAndDontRequeueRecoverer())
                .build()
    }
}