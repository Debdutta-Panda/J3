package com.debduttapanda.j3lib.models

sealed interface J3PermissionStatus {
    data object Granted : J3PermissionStatus
    data class Denied(
        val shouldShowRationale: Boolean
    ) : J3PermissionStatus
}