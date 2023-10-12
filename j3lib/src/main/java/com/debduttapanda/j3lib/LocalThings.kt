package com.debduttapanda.j3lib

import androidx.compose.runtime.compositionLocalOf

val LocalResolver = compositionLocalOf { _Resolver() }
val LocalController =
    compositionLocalOf { Controller(_Resolver(), _NotificationService { _, _ -> }).restricted() }
val LocalNotificationService = compositionLocalOf { _NotificationService { _, _ -> } }
val LocalSuffix = compositionLocalOf { "" }