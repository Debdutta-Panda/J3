package com.debduttapanda.j3lib

class Event<out T>(
    private val content: T,
    val id: Any? = null
) {
    var hasBeenHandled = false
        private set
    fun get(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else {
            null
        }
    }
    fun peekContent(): T = content
}