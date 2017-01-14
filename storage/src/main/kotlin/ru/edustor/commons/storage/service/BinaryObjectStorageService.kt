package ru.edustor.commons.storage.service

import io.minio.ErrorCode
import io.minio.MinioClient
import io.minio.errors.ErrorResponseException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
open class BinaryObjectStorageService(
        @Value("\${edustor.storage.url}") url: String,
        @Value("\${edustor.storage.access-key}") accessKey: String,
        @Value("\${edustor.storage.secret-key}") secretKey: String
) {
    enum class ObjectType(val bucket: String, val extension: String, val contentType: String) {
        PDF_UPLOAD("edustor-pdf-uploads", "pdf", "application/pdf"),
        PAGE("edustor-pages", "pdf", "application/pdf")
    }

    private val minio: MinioClient = MinioClient(url, accessKey, secretKey)

    init {

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

    open fun has(type: ObjectType, id: String): Boolean {
        try {
            minio.statObject(type.bucket, "$id.${type.extension}")
        } catch (e: ErrorResponseException) {
            if (e.errorResponse().errorCode() == ErrorCode.NO_SUCH_OBJECT) {
                return false
            } else {
                throw e
            }
        }
        return true
    }
}