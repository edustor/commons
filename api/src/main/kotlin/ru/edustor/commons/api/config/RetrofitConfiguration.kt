package ru.edustor.commons.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.edustor.commons.api.UploadApi
import java.util.concurrent.TimeUnit

@Configuration
open class RetrofitConfiguration(val objectMapper: ObjectMapper,
                                 @Value("\${edustor.api.token}") token: String,
                                 @Value("\${edustor.api.upload.url}") val uploadBaseUrl: String) {

    val httpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .build()

    companion object {
        lateinit var INTERNAL_AUTH_TOKEN: String
    }

    init {
        INTERNAL_AUTH_TOKEN = token
    }

    @Bean
    open fun uploadApi(): UploadApi {
        val retrofit = Retrofit.Builder()
                .baseUrl(uploadBaseUrl)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
        return retrofit.create(UploadApi::class.java)
    }
}