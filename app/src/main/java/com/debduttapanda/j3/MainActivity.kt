package com.debduttapanda.j3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.debduttapanda.j3lib.MyScreen
import com.debduttapanda.j3.ui.theme.J3Theme
import com.debduttapanda.j3lib.Controller
import com.debduttapanda.j3lib.LocalNotificationService
import com.debduttapanda.j3lib.LocalResolver
import com.debduttapanda.j3lib.NotificationService
import com.debduttapanda.j3lib.Resolver
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
        startDestination = Routes.splash.full
    ) {
        MyScreen(
            navController = navController,
            route = Routes.splash.full,
            { viewModel<SplashViewModel>() }
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
    goBack,
    checkPermission,
    goHome
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
        Spacer(modifier = Modifier
            .fillMaxHeight()
            .weight(1f))
        Button(
            onClick = {
                notifier.notify(MyDataIds.checkPermission)
            }
        ) {
            Text("Check Camera Permission")
        }

    }
}

@Composable
fun SplashPage(
    notifier: NotificationService = com.debduttapanda.j3lib.notifier()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text("Splash")
        Button(
            onClick = {
                notifier.notify(MyDataIds.goHome)
            }
        ) {
            Text("Go Home")
        }
    }
}

@Composable
fun MyView(
    controller: Controller
){
    CompositionLocalProvider(
        LocalResolver provides controller.resolver,
        LocalNotificationService provides controller.notificationService
    ) {
        Column {
            TextField(
                value = "",
                onValueChange = {

                }
            )
            Text("")
            Button(
                onClick = {

                }
            ) {
                Text("Go Back")
            }
        }
    }
}

