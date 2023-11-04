package com.debduttapanda.j3lib

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun BackHandle(
    wirelessViewModel: WirelessViewModelInterface?
) {
    BackHandler(enabled = true, onBack = {
        wirelessViewModel?.onBack()
    })
}
