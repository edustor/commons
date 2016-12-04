package ru.edustor.commons.auth

import ru.edustor.commons.auth.exception.ForbiddenException
import ru.edustor.commons.protobuf.proto.internal.EdustorAccountsProtos

fun EdustorAccountsProtos.EdustorAccount.assertScopeContains(requiredScope: String) {
    if (requiredScope !in this.activeToken.scopeList) {
        throw ForbiddenException("This operation requires '$requiredScope' scope")
    }
}