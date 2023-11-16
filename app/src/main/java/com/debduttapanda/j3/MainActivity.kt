package com.debduttapanda.j3

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.FragmentActivity
import com.debduttapanda.j3.ui.theme.J3Theme
import com.debduttapanda.j3lib.df.Df
import com.debduttapanda.j3lib.localDesignWidth
import dagger.hilt.android.AndroidEntryPoint


fun localeSelection(context: Context, localeTag: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList.forLanguageTags(localeTag)
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(localeTag)
        )
    }
}


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            J3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(localDesignWidth provides 360f) {
                        MyApp()
                    }
                }
            }
        }
    }
}

class MyDf : Df<Boolean>() {
    var title = mutableStateOf("")
    override fun setContent(): @Composable () -> Unit {
        return {
            AlertDialog(
                onDismissRequest = {
                    stop(false)
                    callback(this, "hello", null)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            stop(true)
                        }
                    ) {
                        Text("Ok")
                    }

                },
                dismissButton = {
                    Button(
                        onClick = {
                            callback(this, "cancel", null)
                        }
                    ) {
                        Text("Cancel")
                    }
                },
                title = {
                    Text(title.value)
                }
            )
        }
    }

}