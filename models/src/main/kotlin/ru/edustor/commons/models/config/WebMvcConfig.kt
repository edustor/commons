package ru.edustor.commons.models.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Configuration
import org.springframework.format.Formatter
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.time.LocalDate
import java.util.*

@Configuration
@ConditionalOnWebApplication
open class WebMvcConfig : WebMvcConfigurerAdapter() {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatter(LocalDateFormatter())
    }

    private class LocalDateFormatter : Formatter<LocalDate> {
        override fun parse(text: String, locale: Locale?): LocalDate {
            return LocalDate.ofEpochDay(text.toLong())
        }

        override fun print(`object`: LocalDate, locale: Locale?): String {
            return `object`.toEpochDay().toString()
        }
    }
}