package ru.edustor.commons.auth.model

data class EdustorAuthProfile(
        val accountId: String,
        val scope: List<String>,
        val rawToken: String?
)