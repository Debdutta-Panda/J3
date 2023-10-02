package com.debduttapanda.j3.jerokit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.debduttapanda.j3.DataIds
import com.google.accompanist.systemuicontroller.rememberSystemUiController

data class StatusBarColor(
    val color: Color,
    val darkIcons: Boolean = false
)

@Composable
fun StatusBarColorControl(
    state: StatusBarColor? = safeTState<StatusBarColor>(DataIds.statusBarColor)?.value
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController, state) {
        state?.let {
            val color = it.color
            val darkIcons = it.darkIcons
            systemUiController.setStatusBarColor(
                color = color,
                darkIcons = darkIcons
            )
        }
        onDispose {}
    }
}
