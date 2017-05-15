package ru.edustor.commons.models.rabbit.processing.pages

import java.time.Instant

data class PageRecognizedEvent(
        val uploadUuid: String,
        val pageUuid: String,
        val userId: String,
        val pageIndex: Int, // 1-based
        val totalPageCount: Int,
        val qrUuid: String?,
        val targetLessonId: String?,
        val uploadedTimestamp: Instant?,
        val fileMD5: String
)