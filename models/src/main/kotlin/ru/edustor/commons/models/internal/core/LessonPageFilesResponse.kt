package ru.edustor.commons.models.internal.core

data class LessonPageFilesResponse(
        val ownerId: String,
        val pages: List<String>
)