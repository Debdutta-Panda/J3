package com.debduttapanda.j3lib

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun BackHandle(
    suffix: String,
    notifier: _NotificationService = notifier()
) {
    BackHandler(enabled = true, onBack = {
        notifier.notify(DataIds.back, suffix)
    })
}
