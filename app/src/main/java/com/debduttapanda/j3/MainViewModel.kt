package com.debduttapanda.j3

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debduttapanda.j3.jerokit.Navigation
import com.debduttapanda.j3.jerokit.NotificationService
import com.debduttapanda.j3.jerokit.PermissionHandler
import com.debduttapanda.j3.jerokit.Resolver
import com.debduttapanda.j3.jerokit.ResultingActivityHandler
import com.debduttapanda.j3.jerokit.SoftInputMode
import com.debduttapanda.j3.jerokit.StatusBarColor
import com.debduttapanda.j3.jerokit.WirelessViewModelInterface
import com.debduttapanda.j3.jerokit.scope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

): WirelessViewModelInterface, ViewModel(){
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val resolver = Resolver()
    override val notifier = NotificationService{id, arg ->
        when(id){

        }
    }
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()
    init {
        resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
        )
        _statusBarColor.value = StatusBarColor(
            color = Color.Red,
            darkIcons = true
        )
        setup()
    }

    private fun setup() {
        viewModelScope.launch {
            delay(3000)
            navigation.scope { navHostController, lifecycleOwner, activityService ->
                navHostController.navigate(Routes.home.full)
            }
        }
    }
}