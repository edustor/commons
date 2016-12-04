package ru.edustor.commons.storage.service

import io.minio.MinioClient
import io.minio.errors.ErrorResponseException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
open class BinaryObjectStorageService(
        @Value("\${S3_URL}") url: String,
        @Value("\${S3_ACCESS_KEY}") accessKey: String,
        @Value("\${S3_SECRET_KEY}") secretKey: String
) {
    enum class ObjectType(val bucket: String, val extension: String, val contentType: String) {
        PDF_UPLOAD("edustor-pdf-uploads", "pdf", "application/pdf"),
        PAGE("edustor-pdf-uploads", "pdf", "application/pdf")
    }

    private val minio: MinioClient

    init {
        minio = MinioClient(url, accessKey, secretKey)

        ObjectType.values().forEach { type ->
            if (!minio.bucketExists(type.bucket)) {
                minio.makeBucket(type.bucket)
            }
        }
    }

    open fun get(type: ObjectType, id: String): InputStream? {
        try {
            return minio.getObject(type.bucket, "$id.${type.extension}")
        } catch (e: ErrorResponseException) {
            return null
        }
    }

    open fun put(type: ObjectType, id: String, inputStream: InputStream, size: Long) {
        minio.putObject(type.bucket, "$id.${type.extension}", inputStream, size, type.contentType)
    }

    open fun delete(type: ObjectType, id: String) {
        minio.removeObject(type.bucket, "$id.${type.extension}")
    }
}