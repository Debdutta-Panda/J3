package com.debduttapanda.j3lib

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.debduttapanda.j3lib.models.J3PermissionStatus
import com.debduttapanda.j3lib.models.PermissionRequestResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


public val J3PermissionStatus.isGranted: Boolean
    get() = this == J3PermissionStatus.Granted


public val J3PermissionStatus.shouldShowRationale: Boolean
    get() = when (this) {
        J3PermissionStatus.Granted -> false
        is J3PermissionStatus.Denied -> shouldShowRationale
    }


class J3PermissionState(
    val permission: String,
    val status: J3PermissionStatus
)

class J3MultiPermissionState(
    val permissions: List<J3PermissionState>,
    val revokedPermissions: List<J3PermissionState>,
    val allPermissionsGranted: Boolean,
    val shouldShowRationale: Boolean
)


@OptIn(ExperimentalPermissionsApi::class)
internal class PermissionHandler {
    private val _permissions = mutableStateListOf<String>()
    private val _request = mutableStateOf(false)

    suspend fun check(vararg permissions: String): J3MultiPermissionState? =
        suspendCancellableCoroutine { coroutine ->
            if (permissions.isEmpty()) {
                coroutine.resume(null)
                coroutine.cancel()
                return@suspendCancellableCoroutine
            }
            onResult = {
                onResult = {}
                _permissions.clear()

                val j3mps = J3MultiPermissionState(
                    it.permissions.map {
                        J3PermissionState(
                            it.permission,
                            if (it.status == PermissionStatus.Granted)
                                J3PermissionStatus.Granted
                            else J3PermissionStatus.Denied(it.status.shouldShowRationale)
                        )
                    },
                    it.revokedPermissions.map {
                        J3PermissionState(
                            it.permission,
                            if (it.status == PermissionStatus.Granted)
                                J3PermissionStatus.Granted
                            else J3PermissionStatus.Denied(it.status.shouldShowRationale)
                        )
                    },
                    it.allPermissionsGranted,
                    it.shouldShowRationale
                )

                coroutine.resume(j3mps)
                coroutine.cancel()
            }
            _permissions.addAll(permissions)
        }


    suspend fun request(vararg permissions: String): PermissionRequestResult =
        suspendCancellableCoroutine { coroutine ->
            if (permissions.isEmpty()) {
                coroutine.resume(PermissionRequestResult(null, null))
                coroutine.cancel()
                return@suspendCancellableCoroutine
            }
            onDisposition = { mps, states ->
                onDisposition = { mps, states -> }
                _permissions.clear()
                _request.value = false

                val j3mps = J3MultiPermissionState(
                    mps.permissions.map {
                        J3PermissionState(
                            it.permission,
                            if (it.status == PermissionStatus.Granted)
                                J3PermissionStatus.Granted
                            else J3PermissionStatus.Denied(it.status.shouldShowRationale)
                        )
                    },
                    mps.revokedPermissions.map {
                        J3PermissionState(
                            it.permission,
                            if (it.status == PermissionStatus.Granted)
                                J3PermissionStatus.Granted
                            else J3PermissionStatus.Denied(it.status.shouldShowRationale)
                        )
                    },
                    mps.allPermissionsGranted,
                    mps.shouldShowRationale
                )

                coroutine.resume(PermissionRequestResult(j3mps, states))
                coroutine.cancel()
            }
            _permissions.addAll(permissions)
            _request.value = true
        }

    private var onDisposition: (multiplePermissionsState: MultiplePermissionsState, states: Map<String, Boolean>) -> Unit =
        { mps, states -> }
    private var onResult: (multiplePermissionsState: MultiplePermissionsState) -> Unit = {}

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun handlePermission() {
        if (_permissions.isNotEmpty()) {
            lateinit var permissionState: MultiplePermissionsState
            permissionState = rememberMultiplePermissionsState(
                _permissions
            ) {
                onDisposition(permissionState, it)
            }
            LaunchedEffect(key1 = permissionState) {
                onResult(permissionState)
            }
            LaunchedEffect(key1 = _request) {
                if (_request.value) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        }
    }
}
