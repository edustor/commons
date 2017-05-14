package ru.edustor.commons.models.internal.processing.pages

import java.time.Instant

data class PageRecognizedEvent(
        val uploadUuid: String,
        val pageUuid: String,
        val userId: String,
        val pageIndex: Int,
        val totalPageCount: Int,
        val qrUuid: String?,
        val targetLessonId: String?,
        val uploadedTimestamp: Instant?,
        val fileMD5: String
)