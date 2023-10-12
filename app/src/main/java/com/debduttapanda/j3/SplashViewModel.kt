package com.debduttapanda.j3

import androidx.lifecycle.viewModelScope
import com.debduttapanda.j3lib.WirelessViewModel
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

    }
}