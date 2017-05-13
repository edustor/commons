package ru.edustor.commons.storage.service

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.InputStreamContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
open class BinaryObjectStorageService {
    private val googleTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
    private val jsonFactory = JacksonFactory.getDefaultInstance()

    enum class ObjectType(val extension: String, val contentType: String, val folder: String) {
        PDF_UPLOAD("pdf", "application/pdf", "pages-uploads"),
        PAGE("pdf", "application/pdf", "pages")
    }

    open fun get(accountId: String, type: ObjectType, id: String): InputStream? {
        val drive = getDriveApi(accountId)
        val fileId = findFileId(drive, type, id)
        return drive.files().get(fileId)
                .executeMediaAsInputStream()
    }

    open fun put(accountId: String, type: ObjectType, id: String, inputStream: InputStream, size: Long) {
        val drive = getDriveApi(accountId)

        val parentId = getFolderId(drive, listOf("Edustor", type.folder))

        val gFileMeta = File()
        gFileMeta.name = getFileName(type, id)
        gFileMeta.parents = listOf(parentId)
        val mediaContent = InputStreamContent(type.contentType, inputStream)

        val gFile = drive.files().create(gFileMeta, mediaContent)
                .setFields("id")
                .execute()
    }

    open fun delete(accountId: String, type: ObjectType, id: String) {
        val drive = getDriveApi(accountId)
        val fileId = findFileId(drive, type, id)

        val file = drive.files().get(fileId).execute()
        file.trashed = true

        drive.files().update(fileId, file)
    }

    private fun findFileId(drive: Drive, type: ObjectType, id: String): String? {
        val fileName = getFileName(type, id)
        val q = "name = '$fileName' and trashed = false and mimeType = '${type.contentType}'"
        val searchResult = drive.files().list()
                .setQ(q)
                .setSpaces("drive")
                .setFields("files(id)")
                .execute()

        @Suppress("UNCHECKED_CAST")
        val files = searchResult["files"] as List<Map<String, String>>

        val fileId = files.firstOrNull()?.get("id")

        return fileId
    }

    private fun getDriveApi(accountId: String): Drive {
//        TODO: Call Edustor Accoungit ts for google token
        val accountRefreshToken = ""

        val googleCredential = GoogleCredential.Builder()
                .setJsonFactory(jsonFactory)
                .setTransport(googleTransport)
                .setClientSecrets("99685742253-41uieqd0vl3e03l62c7t3impd38gdt4q.apps.googleusercontent.com",
                        "V5EJEDkJsU46oCGcWF6nKbrh")
                .build()
                .setRefreshToken(accountRefreshToken)
        googleCredential.refreshToken()

        val driveApi = Drive.Builder(googleTransport, jsonFactory, googleCredential)
                .setApplicationName("Edustor")
                .build()

        return driveApi
    }

    private fun getFolderId(drive: Drive,
                            path: List<String>,
                            parent: String = "root"): String? {
        val q = "name = '${path[0]}' and mimeType = 'application/vnd.google-apps.folder' " +
                "and '$parent' in parents and trashed = false"
        val searchResult = drive.files().list()
                .setQ(q)
                .setSpaces("drive")
                .setFields("files(id)")
                .execute()

        @Suppress("UNCHECKED_CAST")
        val files = searchResult["files"] as List<Map<String, String>>

        val fileId = files.firstOrNull()?.get("id") ?: let {
            val folderMeta = File()
            folderMeta.parents = listOf(parent)
            folderMeta.name = path[0]
            folderMeta.mimeType = "application/vnd.google-apps.folder"
            drive.files().create(folderMeta)
                    .setFields("id")
                    .execute()["id"] as String
        }

        if (path.size > 1) {
            return getFolderId(drive, path.drop(1), parent = fileId)
        }

        return fileId
    }


    private fun getFileName(type: ObjectType, id: String): String {
        return when (type) {
            ObjectType.PAGE -> "ep-$id.${type.extension}"
            ObjectType.PDF_UPLOAD -> "upload-$id.${type.extension}"
        }
    }
}