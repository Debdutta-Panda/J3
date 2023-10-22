package com.debduttapanda.j3

import androidx.lifecycle.viewModelScope
import com.debduttapanda.j3lib.EventBusDescription
import com.debduttapanda.j3lib.WirelessViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel: WirelessViewModel(){
    override fun onBack() {

    }

    override fun onStart() {
        viewModelScope.launch {
            delay(3000)
            navigation {
                navigate(Routes.home.navigation {
                    set("userId","123")
                })
            }
        }

    }

    override fun onNotification(id: Any?, arg: Any?) {

    }

    override fun eventBusDescription(): EventBusDescription? {
        return null
    }
}