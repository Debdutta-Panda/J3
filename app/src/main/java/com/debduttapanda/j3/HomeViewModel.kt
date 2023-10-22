package com.debduttapanda.j3

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.debduttapanda.j3lib.AsSync
import com.debduttapanda.j3lib.Controller
import com.debduttapanda.j3lib._NotificationService
import com.debduttapanda.j3lib._Resolver
import com.debduttapanda.j3lib.WirelessViewModel
import com.debduttapanda.j3lib.arguments
import kotlinx.coroutines.launch

class HomeViewModel: WirelessViewModel(){
    val dialogController = Controller()
    val dialogText = mutableStateOf("Hello")
    val showDialog = mutableStateOf(false)
    val onChildCallback: (id: Any, arg: Any?)->Unit = {id,arg->
        when(id){
            MyDataIds.goBack->navigation {
                popBackStack()
            }
            MyDataIds.inputValue->{
                childInputValue.value = arg as String
                childLabelValue.value = "Result1 = "+childInputValue.value
            }
            MyDataIds.checkPermission->{
                //goToAppSettings()
                viewModelScope.launch {
                    val res = MyDialog()
                    Log.d("jfldfkdfdf",res.toString())
                }
            }
        }
    }

    suspend fun MyDialog(): Int {
        return AsSync
            .create<Int>()
            .start(
                dialogController,
            ){a,id,arg->
                when(id){
                    AsSync.Event.START->{
                        showDialog.value = true
                    }
                    0->{
                        showDialog.value = false
                        a.stop(0)
                    }
                    1->{
                        showDialog.value = false
                        a.stop(1)
                    }
                }
            }
    }

    private val childController = Controller(_Resolver(),_NotificationService(onChildCallback))

    private val inputValue = mutableStateOf("")
    private val labelValue = mutableStateOf("")

    private val childInputValue = mutableStateOf("")
    private val childLabelValue = mutableStateOf("")



    override fun onBack() {

    }

    override fun onStart() {
        setStatusBarColor(Color.Red,false)
        navigation {
            Log.d("fkldlfjd",arguments()?.getString("userId")?:"none")
        }
    }

    override fun onNotification(id: Any?, arg: Any?) {
        when(id){
            MyDataIds.goBack->navigation {
                popBackStack()
            }
            MyDataIds.inputValue->{
                inputValue.value = arg as String
                labelValue.value = "Result = "+inputValue.value
            }
            MyDataIds.checkPermission->{
                //goToAppSettings()
                viewModelScope.launch {
                    val permissions = listOf(android.Manifest.permission.CAMERA)
                    permissions.apply {
                        val checked = check()
                        if(checked?.allPermissionsGranted==true){
                            // process()
                        } else{
                            val requested = request()
                            if(requested.multiPermissionState?.allPermissionsGranted==true){
                                // process
                            }
                        }
                    }
                }

            }
        }
    }

    init {
        childController.resolver.addAll(
            MyDataIds.inputValue to childInputValue,
            MyDataIds.labelValue to childLabelValue,

        )
        dialogController.resolver.addAll(
            MyDataIds.dialogText to dialogText,
            MyDataIds.showDialog to showDialog
        )
        controller.resolver.addAll(
            MyDataIds.inputValue to inputValue,
            MyDataIds.labelValue to labelValue,
            MyDataIds.childCrontroller to childController.restricted(),
            MyDataIds.dialogController to dialogController.restricted()
        )
    }

    override fun onCleared() {
        Log.d("flkdjfdfd","cleared")
        super.onCleared()
    }
}