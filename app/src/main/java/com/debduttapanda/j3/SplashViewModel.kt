package com.debduttapanda.j3

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.navigation.navArgument
import com.debduttapanda.j3lib.EventBusDescription
import com.debduttapanda.j3lib.Route
import com.debduttapanda.j3lib.WirelessViewModel
import com.debduttapanda.j3lib.arguments
import com.debduttapanda.j3lib.df.Df
import com.debduttapanda.j3lib.getBack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel: WirelessViewModel(){
    override fun onBack() {

    }

    override fun onNotification(id: Any?, arg: Any?) {

    }

    override fun eventBusDescription(): EventBusDescription? {
        return null
    }

    override fun interCom(message: InterCom) {
        Log.d("fldkfdfdfd","${message.sender},${message.data}")
        interCom<HomeViewModel>("Hi")
    }

    override fun onStartUp(route: Route?, arguments: Bundle?) {

    }

    init {
        viewModelScope.launch {
            delay(2000)
            navigation {
                navigate(Routes.home.full)
            }
        }

    }
}