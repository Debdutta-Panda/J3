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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

): com.debduttapanda.j3lib.WirelessViewModelInterface, ViewModel(){
    private val _statusBarColor = mutableStateOf<com.debduttapanda.j3lib.StatusBarColor?>(null)
    override val softInputMode = mutableStateOf(com.debduttapanda.j3lib.SoftInputMode.adjustNothing)
    override val resolver = com.debduttapanda.j3lib.Resolver()
    override val notifier = com.debduttapanda.j3lib.NotificationService { id, arg ->
        when (id) {
            "${DataIds.back}home" -> {
                navigation.scope { navHostController, lifecycleOwner, activityService ->
                    navHostController.popBackStack()
                }
            }
        }
    }
    override val navigation = com.debduttapanda.j3lib.Navigation()
    override val permissionHandler = com.debduttapanda.j3lib.PermissionHandler()
    override val resultingActivityHandler = com.debduttapanda.j3lib.ResultingActivityHandler()
    init {
        resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
        )
        _statusBarColor.value = com.debduttapanda.j3lib.StatusBarColor(
            color = Color.Red,
            darkIcons = true
        )
    }
}