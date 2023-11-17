package com.debduttapanda.j3lib

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun UINotification(block: suspend CoroutineScope.(id: Any?, event: Event<Any?>?)->Unit){
    val message = rememberUiMessage()
    LaunchedEffect(key1 = message.value){
        block(message.value?.id,message.value)
    }
}

@Composable
fun UINotification(id: Any?, block: suspend CoroutineScope.(event: Event<Any?>?)->Unit){
    val message = rememberUiMessage()
    LaunchedEffect(key1 = message.value){
        if(id == message.value?.id){
            block(message.value)
        }
    }
}