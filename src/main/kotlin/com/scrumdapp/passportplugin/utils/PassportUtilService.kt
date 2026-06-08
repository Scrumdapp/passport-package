package com.scrumdapp.passportplugin.utils

import com.scrumdapp.passportplugin.jwt.PassportService

class PassportUtilService(
    private val passportService: PassportService
) {

    fun checkGroupAccess(groupId: Long): Boolean {
        val passport = passportService.getPassport()
        return passport.userGroups.contains(groupId)
    }
}