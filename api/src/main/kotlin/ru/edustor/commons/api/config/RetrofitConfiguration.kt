package ru.edustor.commons.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.edustor.commons.api.UploadApi

@Configuration
open class RetrofitConfiguration(val objectMapper: ObjectMapper,
                                 @Value("\${AUTH_TOKEN}") token: String) {

    companion object {
        lateinit var INTERNAL_AUTH_TOKEN: String
    }

    init {
        INTERNAL_AUTH_TOKEN = token
    }

    @Bean
    open fun uploadApi(): UploadApi {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://localhost:8083/api/v1/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
        return retrofit.create(UploadApi::class.java)
    }
}