package ru.edustor.commons.version

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class VersionController(val versionInfoHolder: EdustorVersionInfoHolder) {
    @RequestMapping("version")
    fun version(): EdustorVersionInfoHolder {
        return versionInfoHolder
    }
}