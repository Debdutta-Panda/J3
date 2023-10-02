package com.debduttapanda.j3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.debduttapanda.j3lib.MyScreen
import com.debduttapanda.j3.ui.theme.J3Theme
import com.debduttapanda.j3lib.NotificationService
import com.debduttapanda.j3lib.stringState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            J3Theme {
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

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Routes.home.full
    ) {
        MyScreen(
            navController = navController,
            route = Routes.splash.full,
        ) {
            SplashPage()
        }
        MyScreen(
            navController = navController,
            route = Routes.home.full,
            { viewModel<HomeViewModel>() }
        ) {
            HomePage()
        }
    }
}

enum class
MyDataIds {
    inputValue,
    labelValue,
    goBack
}

@Composable
fun HomePage(
    inputValue: String = stringState(key = MyDataIds.inputValue).value,
    labelValue: String = stringState(key = MyDataIds.labelValue).value,
    notifier: NotificationService = com.debduttapanda.j3lib.notifier()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text("Home")
        TextField(
            value = inputValue,
            onValueChange = {
                notifier.notify(MyDataIds.inputValue,it)
            }
        )
        Text(labelValue)
        Button(
            onClick = {
                notifier.notify(MyDataIds.goBack)
            }
        ) {
            Text("Go Back")
        }
    }
}

@Composable
fun SplashPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text("Splash")
    }
}

