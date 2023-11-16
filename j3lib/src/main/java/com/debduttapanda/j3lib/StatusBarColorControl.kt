package com.debduttapanda.j3lib

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.debduttapanda.j3lib.models.StatusBarColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController


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
