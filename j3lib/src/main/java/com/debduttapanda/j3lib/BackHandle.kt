package com.debduttapanda.j3lib

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
internal fun BackHandle(
    wirelessViewModel: WirelessViewModel?
) {
    BackHandler(enabled = true, onBack = {
        wirelessViewModel?.onBack()
    })
}
