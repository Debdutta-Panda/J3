
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
We know splash is not bare bone always. In most cases we need to
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTEzODY4NzU4NTcsLTE4NzMwNjA2MjcsLT
M5NTY1MDQwNiwtMTkzNjk1NTM1MSwtMjA4NDk2NzU1NSwtNzkz
MDk2NzNdfQ==
-->