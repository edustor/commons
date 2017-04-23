package ru.edustor.commons.auth.annotation

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresScope(val scope: String)