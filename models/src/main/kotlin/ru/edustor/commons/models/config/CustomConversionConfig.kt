package ru.edustor.commons.models.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
open class CustomConversionConfig() {
    @Autowired
    fun registerCustomSerializers(mapper: ObjectMapper) {
        val module = SimpleModule("ru.edustor.datatype.custom", Version(1, 0, 0, null, "ru.edustor", "edustor"))
        module.addSerializer(LocalDateJsonSerializer())
        module.addDeserializer(LocalDate::class.java, LocalDateJsonDeserializer())
        mapper.registerModule(module)
    }

    private class LocalDateJsonSerializer : StdScalarSerializer<LocalDate>(LocalDate::class.java) {
        override fun serialize(value: LocalDate, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeNumber(value.toEpochDay())
        }
    }

    private class LocalDateJsonDeserializer : StdScalarDeserializer<LocalDate>(LocalDate::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalDate {
            return LocalDate.ofEpochDay(p.longValue)
        }
    }
}

