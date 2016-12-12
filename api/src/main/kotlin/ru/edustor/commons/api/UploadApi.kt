package ru.edustor.commons.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import ru.edustor.commons.api.config.RetrofitConfiguration

interface UploadApi {
    @FormUrlEncoded
    @POST("internal/nu")
    fun setNextUploadTarget(
            @Field("user_id") userId: String,
            @Field("target") target: String,
            @Header("Authorization") authToken: String = RetrofitConfiguration.INTERNAL_AUTH_TOKEN): retrofit2.Call<Unit>

    @FormUrlEncoded
    @POST("upload/pages/url")
    fun uploadPdfByUrl(
            @Field("url") url: String,
            @Field("uploader_id") uploaderId: String,
            @Header("Authorization") authToken: String = RetrofitConfiguration.INTERNAL_AUTH_TOKEN): retrofit2.Call<Unit>
}