package ru.edustor.commons.models.rabbit.processing.documents

import java.time.Instant

data class DocumentAssembledEvent(
        val requestId: String,
        val documentId: String,
        val timestamp: Instant,
        val succeed: Boolean
)