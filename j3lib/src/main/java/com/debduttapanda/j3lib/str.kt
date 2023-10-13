package com.debduttapanda.j3lib


fun str(activityService: ActivityService, message: Any): String{
        return when (message) {
            is String -> message
            is StringResource -> {
                activityService.stringResource(id = message.id, message.formatArgs)
            }
            is Int-> activityService.stringResource(message)
            else -> "$message"
        }
    }