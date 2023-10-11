package com.debduttapanda.j3

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.debduttapanda.j3lib.WirelessViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Product(
    val id: String,
    val name: String,
    val count: Int
)

class SplashViewModel: WirelessViewModel(){
    override fun onBack() {

    }



    val list = mutableStateListOf<Product>()

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
            "product"->{
                val index = arg as Int
                val item = list[index]
                list[index] = item.copy(count = item.count+1)
            }
        }
    }
}