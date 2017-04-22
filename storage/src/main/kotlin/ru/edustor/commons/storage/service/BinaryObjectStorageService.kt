package ru.edustor.commons.storage.service

import com.mongodb.MongoClient
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import org.springframework.data.mongodb.core.convert.MongoConverter
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsCriteria
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
open class BinaryObjectStorageService(mongoClient: MongoClient, private val converter: MongoConverter) {

    private val factory: SimpleMongoDbFactory = SimpleMongoDbFactory(mongoClient, "edustor-files")

    enum class ObjectType(val extension: String, val contentType: String, val bucket: String) {
        PDF_UPLOAD("pdf", "application/pdf", "pages-uploads"),
        PAGE("pdf", "application/pdf", "fs")
    }

    private fun getGridFs(bucket: String): GridFsTemplate {
        return GridFsTemplate(factory, converter, bucket)
    }

    open fun get(type: ObjectType, id: String): InputStream? {
        return findGridFsFile(type, id)?.inputStream
    }

    open fun put(type: ObjectType, id: String, inputStream: InputStream, size: Long) {
        val gridFs = getGridFs(type.bucket)
        gridFs.store(inputStream, getFileName(type, id), type.contentType)
    }

    open fun delete(type: ObjectType, id: String) {
        val gridFs = getGridFs(type.bucket)
        gridFs.delete(Query.query(GridFsCriteria.whereFilename().`is`(getFileName(type, id))))
    }

    open fun has(type: ObjectType, id: String): Boolean {
        return findGridFsFile(type, id) != null
    }

    private fun findGridFsFile(type: ObjectType, id: String): GridFSDBFile? {
        val gridFs = getGridFs(type.bucket)
        return gridFs.findOne(Query.query(GridFsCriteria.whereFilename().`is`(getFileName(type, id))))
    }

    private fun getFileName(type: ObjectType, id: String): String {
        return when (type) {
            ObjectType.PAGE -> "ep-$id.${type.extension}"
            ObjectType.PDF_UPLOAD -> "upload-$id.${type.extension}"
        }
    }
}