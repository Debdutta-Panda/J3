package com.debduttapanda.j3

import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.FragmentActivity
import com.debduttapanda.j3.ui.theme.J3Theme
import com.debduttapanda.j3lib.df.Df
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


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
    fun runnn(a: Function<*>){
        a
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            J3Theme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

class MyDf: Df<Boolean>() {
    var title = mutableStateOf("")
    override fun setContent(): @Composable () -> Unit {
        return {
            AlertDialog(
                onDismissRequest = {
                    stop(false)
                    callback(this,"hello",null)
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
                            callback(this,"cancel",null)
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