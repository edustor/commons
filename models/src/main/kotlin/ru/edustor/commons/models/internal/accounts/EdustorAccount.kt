package ru.edustor.commons.models.internal.accounts

data class EdustorAccount(
        val uuid: String,
        val activeToken: EdustorToken?
)