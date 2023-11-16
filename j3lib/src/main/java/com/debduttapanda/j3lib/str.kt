package com.debduttapanda.j3lib

import com.debduttapanda.j3lib.models.ActivityService
import com.debduttapanda.j3lib.models.StringResource


internal fun str(activityService: ActivityService, message: Any): String {
    return when (message) {
        is String -> message
        is StringResource -> {
            activityService.stringResource(id = message.id, message.formatArgs)
        }

        is Int -> activityService.stringResource(message)
        else -> "$message"
    }
}