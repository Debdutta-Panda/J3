package com.debduttapanda.j3

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.debduttapanda.j3lib.NavArgumentRequired
import com.debduttapanda.j3lib.Route

object Routes {
    val splash = Route("splash")
    val home = Route("home", listOf(
        navArgument("userId"){
            type = NavType.StringType
        },
        navArgument("dob"){
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }
    ))
}