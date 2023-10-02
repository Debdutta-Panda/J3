package com.debduttapanda.j3lib

import kotlin.collections.MutableList
import kotlin.collections.forEach
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set

class EventCallback{
    fun onEvent(event: Any){
        val toRemove = mutableListOf<Callback>()
        callbacks[event]?.forEach { 
            if(it.owner == this){
                it.block()
            }            
            if(it.lifeTime == LifeTime.OneShot){
                toRemove.add(it)
            }
        }
        toRemove.forEach {
            it.destroy()
        }
    }
    val callbacks = mutableMapOf<Any, MutableList<Callback>>()
    fun remove(callback: Callback){
        callbacks[callback.event]?.remove(callback)
        callback.owner = null
    }
    fun remove(event: Any){
        callbacks[event]?.forEach { it.owner = null }
        callbacks.remove(event)
    }
    fun clear(){
        callbacks.forEach { 
            it.value.forEach { 
                it.owner = null
            }
        }
        callbacks.clear()
    }
    operator fun set(event: Any, callback: Callback){
        if(!callbacks.containsKey(event)){
            callbacks[event] = mutableListOf()
        }
        callbacks[event]?.add(callback)
    }
    fun oneShot(event: Any, block: () -> Unit): Callback {
        val callback = Callback(
            lifeTime = LifeTime.OneShot,
            event = event,
            owner = this,
            block = block
        )
        this[event] = callback
        return callback
    }
    fun repeat(event: Any, block: () -> Unit): Callback {
        val callback = Callback(
            lifeTime = LifeTime.Repeat,
            event = event,
            owner = this,
            block = block
        )
        this[event] = callback
        return callback
    }
    enum class LifeTime{
        OneShot,
        Repeat
    }
    data class Callback(
        var lifeTime: LifeTime,
        var event: Any,
        var block: ()->Unit,
        var owner: EventCallback?
    ){
        fun destroy(){
            owner?.remove(this)
        }
    }
}