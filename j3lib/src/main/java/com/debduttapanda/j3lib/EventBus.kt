package com.debduttapanda.j3lib

import org.intellij.lang.annotations.Language

class EventBus {
    companion object {
        val instance by lazy { EventBus() }
    }

    val callbacks = mutableMapOf<String, Callback>()

    data class Callback(
        val topics: MutableList<String>,
        val action: (String, String, Any?) -> Unit
    )

    fun notify(
        topic: String,
        value: Any? = null,
        excludeIds: List<String> = emptyList()
    ) {
        val list = if (excludeIds.isEmpty()) {
            callbacks
        } else {
            callbacks.minus(excludeIds)
        }
        list
            .forEach {
                it.value.topics.forEach { pattern ->
                    if (pattern.toRegex().matches(topic)) {
                        it.value.action(pattern, topic, value)
                    }
                }
            }
    }

    fun MutableList<String>.pattern(@Language("regexp") pattern: String) {
        add(pattern)
    }

    class TopicsBuilder(private val list: MutableList<String>){
        fun add(@Language("regexp") pattern: String): TopicsBuilder{
            list.add(pattern)
            return this
        }
    }

    fun register(
        id: String,
        action: (pattern: String, topic: String, value: Any?) -> Unit,
        topics: (TopicsBuilder.() -> Unit)? = null
    ) {
        val list = mutableListOf<String>()
        val builder = TopicsBuilder(list)
        topics?.invoke(builder)
        callbacks[id] = Callback(list, action)
    }

    fun topics(id: String, list: MutableList<String>.() -> Unit) {
        val topics = callbacks[id]?.topics
        topics?.let {
            list(it)
        }
    }

    fun unregister(id: String?) {
        callbacks.remove(id)
    }
}