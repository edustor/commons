package ru.edustor.commons.models.rabbit.processing.pages

data class PageProcessedEvent(
        val userId: String,

        val uploadUuid: String,
        val totalPageCount: Int?,

        val pageIndex: Int,
        val pageUuid: String,
        val qrUuid: String?,

        val success: Boolean,
        val targetLessonId: String?,
        val targetLessonName: String?
)