package com.debduttapanda.j3

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debduttapanda.j3lib.Navigation
import com.debduttapanda.j3lib.NotificationService
import com.debduttapanda.j3lib.PermissionHandler
import com.debduttapanda.j3lib.Resolver
import com.debduttapanda.j3lib.ResultingActivityHandler
import com.debduttapanda.j3lib.SoftInputMode
import com.debduttapanda.j3lib.StatusBarColor
import com.debduttapanda.j3lib.WirelessViewModel
import com.debduttapanda.j3lib.WirelessViewModelInterface
import com.debduttapanda.j3lib.scope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel: WirelessViewModel(){
    private val inputValue = mutableStateOf("")
    private val labelValue = mutableStateOf("")
    override fun onBack() {

    }

    override fun onStart() {
        setStatusBarColor(Color.Red,false)
    }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onNotification(id: Any?, arg: Any?) {
        when(id){
            MyDataIds.goBack->navigate {
                popBackStack()
            }
            MyDataIds.inputValue->{
                inputValue.value = arg as String
                labelValue.value = "Result = "+inputValue.value
            }
            MyDataIds.checkPermission->{
                goToAppSettings()
                /*viewModelScope.launch(Dispatchers.Main){
                    val permitted = android.Manifest.permission.CAMERA.permitted()?.allPermissionsGranted?:false
                    if(!permitted){
                        val (mps,states) = android.Manifest.permission.CAMERA.requestPermission()
                        val rationaleNeeded = mps?.shouldShowRationale
                        toast(states.toString())
                    }
                }*/
            }
        }
    }

    override fun addResolverData(resolver: Resolver) {
        resolver.addAll(
            MyDataIds.inputValue to inputValue,
            MyDataIds.labelValue to labelValue,
        )
    }
}