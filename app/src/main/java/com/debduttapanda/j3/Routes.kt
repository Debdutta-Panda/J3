package com.debduttapanda.j3

object Routes {
    data class Route(
        val name: String,
        val args: String = ""
    ) {
        val full get() = "$name$args"
    }

    val splash = Route("splash")
    val home = Route("home")
}