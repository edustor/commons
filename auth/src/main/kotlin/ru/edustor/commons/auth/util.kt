package ru.edustor.commons.auth

import ru.edustor.commons.auth.exception.ForbiddenException
import ru.edustor.commons.models.internal.accounts.EdustorAccount

fun EdustorAccount.assertScopeContains(vararg requiredScope: String) {
    this.activeToken ?: let {
        throw ForbiddenException("This operation requires active token")
    }

    val missingScope = requiredScope.subtract(this.activeToken!!.scope.asIterable())
    if (missingScope.isNotEmpty()) {
        throw ForbiddenException("This operation requires '$requiredScope' scope")
    }
}