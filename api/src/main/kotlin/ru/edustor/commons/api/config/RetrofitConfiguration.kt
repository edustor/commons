package ru.edustor.commons.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.edustor.commons.api.CoreApi
import ru.edustor.commons.api.UploadApi
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

@Configuration
open class RetrofitConfiguration(val objectMapper: ObjectMapper,
                                 private val environment: Environment) {

    val httpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .build()

    companion object {
        lateinit var INTERNAL_AUTH_TOKEN: String
    }

    init {
        INTERNAL_AUTH_TOKEN = environment.getRequiredProperty("edustor.api.token")
    }

    @Bean
    @ConditionalOnProperty("edustor.api.storage.url")
    open fun uploadApi(): UploadApi {
        return buildRetrofitApi(UploadApi::class.java, environment.getRequiredProperty("edustor.api.storage.url"))
    }

    @Bean
    @ConditionalOnProperty("edustor.api.core.url")
    open fun coreApi(): CoreApi {
        return buildRetrofitApi(CoreApi::class.java, environment.getRequiredProperty("edustor.api.core.url"))
    }

    private fun <T> buildRetrofitApi(clazz: Class<T>, baseUrl: String): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
        return retrofit.create(clazz)
    }
}