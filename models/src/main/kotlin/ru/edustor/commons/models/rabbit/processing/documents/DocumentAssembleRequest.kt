package ru.edustor.commons.models.rabbit.processing.documents

data class DocumentAssembleRequest(
        val documentId: String,
        val pages: List<Page>
) {
    data class Page(
            val fileId: String
    )
}