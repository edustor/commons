package ru.edustor.commons.auth.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException(msg: String, cause: Throwable? = null) : Exception(msg, cause)