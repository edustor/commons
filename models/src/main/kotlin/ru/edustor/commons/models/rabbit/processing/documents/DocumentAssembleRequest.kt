package ru.edustor.commons.models.rabbit.processing.documents

import java.time.Instant
import java.util.*

data class DocumentAssembleRequest(
        val documentId: String,
        val pages: List<Page>,
        val timestamp: Instant = Instant.now(),
        val requestId: String = UUID.randomUUID().toString()
        ) {
    data class Page(
            val fileId: String
    )
}