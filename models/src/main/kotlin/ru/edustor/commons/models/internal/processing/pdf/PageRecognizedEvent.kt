package ru.edustor.commons.models.internal.processing.pdf

import java.time.Instant

data class PageRecognizedEvent(
        val uploadUuid: String,
        val pageUuid: String,
        val userId: String,
        val pageIndex: Int?,
        val qrUuid: String?,
        val targetLessonId: String?,
        val uploadedTimestamp: Instant?
)