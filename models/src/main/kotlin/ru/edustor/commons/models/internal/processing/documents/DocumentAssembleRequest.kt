package ru.edustor.commons.models.internal.processing.documents

data class DocumentAssembleRequest(
        val lessonId: String,
        val pages: List<Page>
) {
    data class Page(
            val fileId: String
    )
}