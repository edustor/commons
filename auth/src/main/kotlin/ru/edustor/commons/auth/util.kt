package ru.edustor.commons.auth

import ru.edustor.commons.auth.exception.ForbiddenException
import ru.edustor.commons.models.internal.accounts.EdustorAccount

fun EdustorAccount.assertScopeContains(requiredScope: String) {
    if (this.activeToken != null && requiredScope !in this.activeToken!!.scope) {
        throw ForbiddenException("This operation requires '$requiredScope' scope")
    }
}