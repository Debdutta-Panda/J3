package com.debduttapanda.j3lib.models

import com.debduttapanda.j3lib.J3MultiPermissionState

data class PermissionRequestResult(
    val multiPermissionState: J3MultiPermissionState?,
    val permittedMap: Map<String, Boolean>?
)