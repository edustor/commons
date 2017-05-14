package ru.edustor.commons.models.internal.processing.pages

import java.time.Instant

data class PdfUploadedEvent(
        val uuid: String,
        val userId: String,
        val timestamp: Instant?,
        val targetLessonId: String?
)