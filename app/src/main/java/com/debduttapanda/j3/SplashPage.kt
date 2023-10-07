package com.debduttapanda.j3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.debduttapanda.j3lib._NotificationService

@Composable
fun SplashPage(
    notifier: _NotificationService = com.debduttapanda.j3lib.notifier()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text("Splash")
        Button(
            onClick = {
                notifier.notify(MyDataIds.goHome)
            }
        ) {
            Text("Go Home")
        }
    }
}