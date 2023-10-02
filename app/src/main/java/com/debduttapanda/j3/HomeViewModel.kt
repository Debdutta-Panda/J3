package com.debduttapanda.j3

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.debduttapanda.j3lib.Navigation
import com.debduttapanda.j3lib.NotificationService
import com.debduttapanda.j3lib.PermissionHandler
import com.debduttapanda.j3lib.Resolver
import com.debduttapanda.j3lib.ResultingActivityHandler
import com.debduttapanda.j3lib.SoftInputMode
import com.debduttapanda.j3lib.StatusBarColor
import com.debduttapanda.j3lib.WirelessViewModelInterface
import com.debduttapanda.j3lib.scope


class HomeViewModel: WirelessViewModelInterface, ViewModel(){
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val resolver = Resolver()
    override val notifier = NotificationService { id, arg ->
        when (id) {
            "${DataIds.back}home" -> {
                navigation.scope { navHostController, lifecycleOwner, activityService ->
                    navHostController.popBackStack()
                }
            }
            MyDataIds.inputValue->{
                inputValue.value = arg as String
                labelValue.value = "Result = "+inputValue.value
            }
            MyDataIds.goBack->{
                navigation.scope { navHostController, lifecycleOwner, activityService ->
                    navHostController.popBackStack()
                }
            }
        }
    }
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    private val inputValue = mutableStateOf("")
    private val labelValue = mutableStateOf("")
    init {
        resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
            MyDataIds.inputValue to inputValue,
            MyDataIds.labelValue to labelValue,
        )
        _statusBarColor.value = StatusBarColor(
            color = Color.Red,
            darkIcons = true
        )
    }
}