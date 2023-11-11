package com.debduttapanda.j3lib

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun <I,O>ResultingActivityHandler.requestPermission(
    createIntent: (context: Context, input: I)->Intent,
    parseResult: (resultCode: Int, intent: Intent?)->O,
    launch: ManagedActivityResultLauncher<I,O>.()->Unit,
    maxTry: Int = 10,
    millis: Long = 200
): O?{
    return request(
        object : ActivityResultContract<I, O>() {
            override fun createIntent(context: Context, input: I): Intent {
                return createIntent(context, input)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): O {
                return parseResult(resultCode, intent)
            }
        },
        maxTry,
        millis
    ){
        launch(it)
    }
}

suspend fun ResultingActivityHandler.takePicturePreview(
    maxTry: Int = 10,
    millis: Long = 200
): Bitmap?{
    return request(
        ActivityResultContracts
            .TakePicturePreview(),
        maxTry,
        millis
    ){
        it.launch()
    }
}
suspend fun ResultingActivityHandler.getContent(
    type: String,
    maxTry: Int = 10,
    millis: Long = 200
): Uri?{
    return request(
        ActivityResultContracts.GetContent(),
        maxTry,
        millis
    ){
        it.launch(type)
    }
}

suspend fun ResultingActivityHandler.intentContract(
    intent: Intent,
    maxTry: Int = 10,
    millis: Long = 200
): ActivityResult?{
    return request(
        ActivityResultContracts.StartActivityForResult(),
        maxTry,
        millis
    ){
        it.launch(intent)
    }
}


class ResultingActivityHandler {
    private var _callback = mutableStateOf<(@Composable () -> Unit)?>(null)

    suspend fun <I, O> request(
        contract: ActivityResultContract<I, O>,
        maxTry: Int = 10,
        millis: Long = 200,
        launcher: (ManagedActivityResultLauncher<I, O>) -> Unit
    ): O? = suspendCancellableCoroutine {coroutine ->
        _callback.value = {
            val a = rememberLauncherForActivityResult(
                contract
            ) {
                coroutine.resume(it)
                _callback.value = null
                coroutine.cancel()
                return@rememberLauncherForActivityResult
            }

            LaunchedEffect(a){
                var tried = 0
                var tryOn = true
                while (tryOn){
                    ++tried
                    delay(millis)
                    try {
                        launcher(a)
                        tryOn = false
                    } catch (e: Exception) {
                        if (tried > maxTry){
                            tryOn = false
                            coroutine.resume(null)
                            _callback.value = null
                            coroutine.cancel()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun handle() {
        if (_callback.value != null){
            _callback.value?.invoke()
        }
    }
}
