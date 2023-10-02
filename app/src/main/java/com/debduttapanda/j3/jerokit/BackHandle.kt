package com.debduttapanda.j3.jerokit

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.debduttapanda.j3.DataIds
import com.debduttapanda.j3.jerokit.NotificationService
import com.debduttapanda.j3.jerokit.notifier

@Composable
fun BackHandle(
    suffix: String,
    notifier: NotificationService = notifier()
) {
    BackHandler(enabled = true, onBack = {
        notifier.notify("${DataIds.back}$suffix", null)
    })
}
