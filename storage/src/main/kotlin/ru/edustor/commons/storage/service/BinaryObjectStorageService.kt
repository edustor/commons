package ru.edustor.commons.storage.service

import com.mongodb.MongoClient
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import org.springframework.data.mongodb.core.convert.MongoConverter
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsCriteria
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
open class BinaryObjectStorageService(mongoClient: MongoClient, converter: MongoConverter) {

    val gridFs: GridFsOperations

    init {
        val factory = SimpleMongoDbFactory(mongoClient, "edustor-files")
        gridFs = GridFsTemplate(factory, converter)
    }

    enum class ObjectType(val extension: String, val contentType: String) {
        PDF_UPLOAD("pdf", "application/pdf"),
        PAGE("pdf", "application/pdf")
    }

    open fun get(type: ObjectType, id: String): InputStream? {
        return findGridFsFile(type, id)?.inputStream
    }

    open fun put(type: ObjectType, id: String, inputStream: InputStream, size: Long) {
        gridFs.store(inputStream, getFileName(type, id), type.contentType)
    }

    open fun delete(type: ObjectType, id: String) {
        gridFs.delete(Query.query(GridFsCriteria.whereFilename().`is`(getFileName(type, id))))
    }

    open fun has(type: ObjectType, id: String): Boolean {
        return findGridFsFile(type, id) != null
    }

    private fun findGridFsFile(type: ObjectType, id: String): GridFSDBFile? {
        return gridFs.findOne(Query.query(GridFsCriteria.whereFilename().`is`(getFileName(type, id))))
    }

    private fun getFileName(type: ObjectType, id: String): String {
        return when (type) {
            ObjectType.PAGE -> "ep-$id.${type.extension}"
            ObjectType.PDF_UPLOAD -> "upload-$id.${type.extension}"
        }
    }
}