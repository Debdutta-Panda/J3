package com.debduttapanda.j3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.debduttapanda.j3lib.LocalController
import com.debduttapanda.j3lib._NotificationService
import com.debduttapanda.j3lib.listState

@Composable
fun SplashPage(
    notifier: _NotificationService = com.debduttapanda.j3lib.notifier(),
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