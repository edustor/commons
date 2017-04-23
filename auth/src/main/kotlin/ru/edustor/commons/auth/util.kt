package ru.edustor.commons.auth

import ru.edustor.commons.auth.exception.ForbiddenException
import ru.edustor.commons.auth.model.EdustorAuthProfile

fun EdustorAuthProfile.assertScopeContains(vararg requiredScope: String) {

    val missingScope = requiredScope.subtract(this.scope.asIterable())
    if (missingScope.isNotEmpty()) {
        throw ForbiddenException("This operation requires $missingScope scope")
    }
}