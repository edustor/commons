package ru.edustor.commons.storage.service

import io.minio.MinioClient
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
open class BinaryObjectStorageService(environment: Environment) {

    val minioClient: MinioClient

    init {
        val host = environment.getProperty("minio.url") ?: "http://localhost:9000"
        val accessKey = environment.getRequiredProperty("minio.access-key")
        val secretKey = environment.getRequiredProperty("minio.secret-key")

        minioClient = MinioClient(host, accessKey, secretKey)

        ObjectType.values()
                .flatMap { listOf(it.bucket, it.bucket + "-removed") }
                .forEach { ensureBucket(it) }
    }

    open fun get(type: ObjectType, id: String): InputStream? {
        return minioClient.getObject(type.bucket, getFileName(type, id))
    }

    fun stat(type: ObjectType, id: String): ObjectStat {
        val objectStat = minioClient.statObject(type.bucket, getFileName(type, id))

        return ObjectStat(id, type, objectStat.length(), objectStat.contentType())

    }

    open fun put(type: ObjectType, id: String, inputStream: InputStream, size: Long) {
        minioClient.putObject(type.bucket, getFileName(type, id), inputStream, size, type.contentType)
    }

    open fun delete(type: ObjectType, id: String) {
        minioClient.copyObject(type.bucket, getFileName(type, id), type.bucket + "-removed")
        minioClient.removeObject(type.bucket, getFileName(type, id))
    }

    enum class ObjectType(val extension: String, val contentType: String, val bucket: String) {
        PDF_UPLOAD("pdf", "application/pdf", "pages-uploads"),
        PAGE("pdf", "application/pdf", "pages"),
        ASSEMBLED_DOCUMENT("pdf", "application/pdf", "assembled-documents")
    }


    private fun getFileName(type: ObjectType, id: String): String {
        return when (type) {
            ObjectType.PAGE -> "ep-$id.${type.extension}"
            ObjectType.PDF_UPLOAD -> "upload-$id.${type.extension}"
            ObjectType.ASSEMBLED_DOCUMENT -> "document-$id.${type.extension}"
        }
    }

    private fun ensureBucket(name: String) {
        if (!minioClient.bucketExists(name)) {
            minioClient.makeBucket(name)
        }
    }

    data class ObjectStat(val id: String, val type: ObjectType, val length: Long, val contentType: String)
}