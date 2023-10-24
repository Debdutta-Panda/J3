package com.debduttapanda.j3lib.df

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

abstract class Df<T>{
    var callback: (df: Df<T>,topic: Any, value: Any?)->Unit = {df,t,v->}
    private set
    private var cdf: ComposeDialogFragment? = null
    abstract fun setContent(): @Composable ()->Unit

    private fun ensure(){
        if(cdf==null){
            cdf = ComposeDialogFragment(setContent())
        }
    }

    private fun show(fm: FragmentManager, tag: String){
        ensure()
        cdf?.show(fm,tag)
    }



    private fun dismiss(){
        cdf?.dismiss()
        cdf = null
    }

    private var onStopCallback: (value: T)->Unit = {}

    fun stop(value: T){
        dismiss()
        onStopCallback(value)
    }

    internal suspend fun start(fm: FragmentManager, tag: String, block: (df: Df<T>,topic: Any, value: Any?)->Unit) = suspendCancellableCoroutine {coroutine->
        callback = block
        onStopCallback = {
            onStopCallback = {}
            coroutine.isActive
            coroutine.resume(it)
            coroutine.cancel()
        }
        show(fm, tag)
    }

    /*companion object{
        inline fun <reified D : Df<T>> start(tag: String, block: (df: D,topic: Any, value: Any?)->Unit): T {
            dfer(D::class.java.getDeclaredConstructor().newInstance(),tag, block)
        }


    }*/
}
class ComposeDialogFragment(private val content: @Composable ()->Unit): DialogFragment(){
    override fun onStart() {
        super.onStart()
        dialog?.window?.setDimAmount(0f)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                content()
            }
        }
    }
}