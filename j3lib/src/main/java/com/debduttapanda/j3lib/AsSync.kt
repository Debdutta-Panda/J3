package com.debduttapanda.j3lib

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AsSync<T : Any> private constructor(){

    private var callback: (
        d: AsSync<T>,
        id: Any, arg: Any?
    )->Unit = {_,_,_->}

    companion object{
        fun <T : Any>create(): AsSync<T>{
            return AsSync()
        }
    }

    suspend fun start(
        controller: Controller,
        callback: (
            d: AsSync<T>,
            id: Any, arg: Any?
        )->Unit
    ): T = suspendCancellableCoroutine {coroutine ->
        this.callback = callback
        controller.notificationService.callback = {id,arg->
            this.callback(this,id,arg)
        }
        onStopCallback = {
            onStopCallback = {}
            coroutine.resume(it)
            coroutine.cancel()
        }
        this.callback(this,Event.START,null)
    }

    private var onStopCallback: (value: T)->Unit = {}

    fun stop(value: T){
        onStopCallback(value)
    }

    enum class Event{
        START
    }
}
