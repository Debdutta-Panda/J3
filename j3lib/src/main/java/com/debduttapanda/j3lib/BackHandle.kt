package com.debduttapanda.j3lib

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.debduttapanda.j3.DataIds

@Composable
fun BackHandle(
    suffix: String,
    notifier: NotificationService = notifier()
) {
    BackHandler(enabled = true, onBack = {
        notifier.notify("${DataIds.back}$suffix", null)
    })
}
