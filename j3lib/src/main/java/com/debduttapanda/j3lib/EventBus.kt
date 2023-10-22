package com.debduttapanda.j3lib

class EventBus{
        companion object{
            val instance by lazy { EventBus() }
        }
        val callbacks = mutableMapOf<String,Callback>()
        data class Callback(
            val topics: MutableList<String>,
            val action: (String,String,Any?)->Unit
        )
        fun notify(
            topic: String,
            value: Any? = null,
            excludeIds: List<String> = emptyList()
        ){
            callbacks
                .apply {
                    if(excludeIds.isNotEmpty()){
                        minus(excludeIds)
                    }
                }
                .forEach {
                    it.value.topics.forEach {pattern->
                        if(pattern.toRegex().matches(topic)){
                            it.value.action(pattern,topic,value)
                        }
                    }
                }
        }
        fun register(
            id: String,
            action: (pattern: String, topic: String, value: Any?) -> Unit,
            topics: (MutableList<String>.()->Unit)? = null
        ){
            callbacks[id] = Callback(mutableListOf<String>().apply { topics?.invoke(this) }, action)
        }
        fun topics(id: String, list: MutableList<String>.()->Unit){
            val topics = callbacks[id]?.topics
            topics?.let {
                list(it)
            }
        }
        fun unregister(id: String?){
            callbacks.remove(id)
        }
    }