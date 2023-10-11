
# J3
[![](https://jitpack.io/v/Debdutta-Panda/J3.svg)](https://jitpack.io/#Debdutta-Panda/J3)

Jetpack Compose Material 3 Architectural Utility Library

## Installation
Add it in your root build.gradle at the end of repositories:

### Groovy

```groovy
allprojects {
	repositories { 
		...
		maven { url 'https://jitpack.io' }
	}
}
```
### Kts
```kts
allprojects {
	repositories {
		...
		maven("https://jitpack.io")
	}
}
```
Add the dependency
### Groovy
```groovy
dependencies {
	implementation 'com.github.Debdutta-Panda:J3:<version>'
}
```
### Kts
```kts
dependencies {
	implementation("com.github.Debdutta-Panda:J3:<version>")
}
```

## Update App

* kotlin version: 1.9.10
* compileSdk: 34
* targetSdk: 34
* java version: 17
* compose compiler version: 1.5.3
* compose bom version: 2023.10.00

Otherwise J3 lib may conflict or give some error.

## Create a simple splash screen

```
@Composable  
fun SplashPage() {  
    Column(  
        modifier = Modifier  
            .fillMaxSize(),  
  verticalArrangement = Arrangement.Center,  
  horizontalAlignment = Alignment.CenterHorizontally  
  ){  
  Text("Splash")  
        Spacer(modifier = Modifier.height(24.dp))  
        CircularProgressIndicator()  
    }  
}
```

Nothing Special is here.
We know splash is not bare bone always. In most cases we need to do(fetch, send, etc). In most simple case we need to do just a wait go to some page(login, intro, home, etc.)
So, we need to do something of course. But those need to be controlled/triggered from/by viewModel.
Let's see how we can do this by J3.

### Create a MyApp

```
@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Routes.splash.full
    ) {
        MyScreen(
            navController = navController,
            route = "splash",
            { viewModel<SplashViewModel>() }
        ) {
            SplashPage()
        }
        MyScreen(
            navController = navController,
            route = "home",
            { viewModel<HomeViewModel>() }
        ) {
            HomePage()
        }
    }
}
```

Here we have created two pages splash and home. The plan is to stay a while in splash then go to home. You can see we need to viewModel also for those pages.

To use `viewModel<T>` you need to add the following dependency

```
val nav_version = "2.7.4"  
implementation("androidx.navigation:navigation-compose:$nav_version")
```

### SplashViewModel

```
class SplashViewModel: WirelessViewModel(){  
    override fun onBack() {  
          
    }  
  
    override fun onStart() {  
          
    }  
  
    override fun onNotification(id: Any?, arg: Any?) {  
          
    }  
}
```

The viewModel need to extend `WirelessViewModel`. You will be forced to implement `onBack`, `onStart`, `onNotification`. Here we are interested in `onStart`.

onStart will be called when the viewModel is starting.
Here we have scope to do something. We need to do just a wait and navigate to home page.

```
viewModelScope.launch {
    delay(3000)
    navigate {
        navigate(Routes.home.full)
    }
}
```

Just put this code inside `onStart`. And you are done.
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTExNzkyNDkwNCwtMTg3MzA2MDYyNywtMz
k1NjUwNDA2LC0xOTM2OTU1MzUxLC0yMDg0OTY3NTU1LC03OTMw
OTY3M119
-->