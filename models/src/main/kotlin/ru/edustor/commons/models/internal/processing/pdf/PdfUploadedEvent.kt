package ru.edustor.commons.models.internal.processing.pdf

import java.time.Instant

data class PdfUploadedEvent(
        val uuid: String,
        val userId: String,
        val timestamp: Instant?,
        val targetLessonId: String?
)