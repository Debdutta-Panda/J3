package com.debduttapanda.j3lib.models

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.debduttapanda.j3lib.ContextConsumer
import com.debduttapanda.j3lib.NotFragmentActivityException
import com.debduttapanda.j3lib.df.Df

data class ActivityService(
    private val context: Context
) {
    fun finish(){
        (context as Activity).finish()
    }
    fun toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        (context as Activity).runOnUiThread {
            Toast.makeText(context, message, duration).show()
        }
    }

    fun stringResource(@StringRes id: Int, vararg formatArgs: Any): String {
        return context.getString(id, formatArgs)
    }

    fun myAppSettingsPage() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun letConsumeContext(contextConsumer: ContextConsumer) {
        contextConsumer.consume(context)
    }

    internal suspend fun <T> showDf(
        df: Df<T>,
        tag: String,
        block: ((df: Df<T>, topic: Any, value: Any?) -> Unit)? = null
    ): T {
        if (context !is FragmentActivity) {
            throw NotFragmentActivityException()
        }
        val r = df.start(context.supportFragmentManager, tag, block)
        return r
    }

    fun contentResolver(): ContentResolver = context.contentResolver
    //fun context(): Context = context
}