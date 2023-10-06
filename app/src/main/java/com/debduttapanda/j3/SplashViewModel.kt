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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel: WirelessViewModel(){
    override fun onBack() {

    }

    override fun onStart() {
        viewModelScope.launch {
            delay(3000)
            navigate {
                navigate(Routes.home.full)
            }
        }
    }

    override fun onNotification(id: Any?, arg: Any?) {
        when(id){
            MyDataIds.goHome->{
                navigate {
                    navigate(Routes.home.full)
                }
            }
        }
    }
}