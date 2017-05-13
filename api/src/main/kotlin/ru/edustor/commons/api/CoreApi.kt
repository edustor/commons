package ru.edustor.commons.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import ru.edustor.commons.api.config.RetrofitConfiguration
import ru.edustor.commons.models.internal.core.LessonPageFilesResponse

interface CoreApi {
    @GET("internal/lessons/{lessonId}/pages/files")
    fun getLessonPageFiles(
            @Path("lessonId") lessonId: String,
            @Header("Authorization") authToken: String = RetrofitConfiguration.INTERNAL_AUTH_TOKEN
    ): retrofit2.Call<LessonPageFilesResponse>
}