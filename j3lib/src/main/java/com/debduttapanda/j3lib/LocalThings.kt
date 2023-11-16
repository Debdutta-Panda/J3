package com.debduttapanda.j3lib

import androidx.compose.runtime.compositionLocalOf
import com.debduttapanda.j3lib.models._NotificationService

internal val LocalResolver = compositionLocalOf { _Resolver() }
internal val LocalController =
    compositionLocalOf { Controller(_Resolver(), _NotificationService { _, _ -> }).restricted() }
internal val LocalNotificationService = compositionLocalOf { _NotificationService { _, _ -> } }
internal val LocalSuffix = compositionLocalOf { "" }