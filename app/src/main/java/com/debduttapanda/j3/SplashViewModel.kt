package com.debduttapanda.j3

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.debduttapanda.j3lib.EventBusDescription
import com.debduttapanda.j3lib.WirelessViewModel
import com.debduttapanda.j3lib.df.Df
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

    init {
        viewModelScope.launch {
            val mdf = MyDf()
            mdf.title.value = "Hello"
            mdf.title
            val a = mdf.start("")
            Log.d("flkdfddfdf",a.toString())
        }
    }

    override fun onNotification(id: Any?, arg: Any?) {

    }

    override fun eventBusDescription(): EventBusDescription? {
        return null
    }
}