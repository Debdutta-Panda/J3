package com.debduttapanda.j3lib

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@OptIn(ExperimentalPermissionsApi::class)
class PermissionHandler{
    private val _permissions = mutableStateListOf<String>()
    private val _request = mutableStateOf(false)

    suspend fun check(vararg permissions: String): MultiplePermissionsState? =
        suspendCancellableCoroutine {coroutine ->
            if (permissions.isEmpty()){
                coroutine.resume(null)
                return@suspendCancellableCoroutine
            }
            onResult = {
                onResult = {}
                _permissions.clear()
                coroutine.resume(it)
            }
            _permissions.addAll(permissions)
        }

    suspend fun request(vararg permissions: String): Pair<MultiplePermissionsState?,Map<String, Boolean>?> =
        suspendCancellableCoroutine {coroutine ->
            if (permissions.isEmpty()){
                coroutine.resume(Pair(null,null))
                return@suspendCancellableCoroutine
            }
            onDisposition = {mps,states->
                onDisposition = {mps,states->}
                _permissions.clear()
                _request.value = false
                coroutine.resume(Pair(mps,states))
            }
            _permissions.addAll(permissions)
            _request.value = true
        }

    private var onDisposition: (multiplePermissionsState: MultiplePermissionsState,states: Map<String, Boolean>) -> Unit = {mps,states->}
    private var onResult: (multiplePermissionsState: MultiplePermissionsState) -> Unit = {}

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun handlePermission() {
        if (_permissions.isNotEmpty()){
            lateinit var permissionState: MultiplePermissionsState
            permissionState = rememberMultiplePermissionsState(
                _permissions
            ){
                onDisposition(permissionState,it)
            }
            LaunchedEffect(key1 = permissionState){
                onResult(permissionState)
            }
            LaunchedEffect(key1 = _request){
                if (_request.value){
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        }
    }
}
