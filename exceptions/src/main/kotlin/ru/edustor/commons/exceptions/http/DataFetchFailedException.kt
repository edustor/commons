package ru.edustor.commons.exceptions.http

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.ServletException

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class DataFetchFailedException(override val message: String? = null, override val cause: Throwable? = null)
    : ServletException(message, cause)
