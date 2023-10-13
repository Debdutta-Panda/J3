
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

Creat the `HomPage` and `HomeViewModel` like `SplashPage` and `SplashViewModel`.

onStart will be called when the viewModel is starting.
Here we have scope to do something. We need to do just a wait and navigate to home page.

```
viewModelScope.launch {
    delay(3000)
    navigate {
        navigate("home")
    }
}
```

Just put this code inside `onStart`. And you are done.

The problem was to navigate by viewModel. It is really hard to get access of navigation things in viewModel.

J3's first benefit is to get navigation controll in viewModel without having any extra/complex/bad things.

Navigation in viewModel is just easy in J3.
Inside `navigate{}` you will have all the functionalities of `navHostController`.

## Improve HomePage and its viewModel

Let's suppose we will have a textField, a button and a text in home page. The plan is to type in the textfield, whatever in textField it will be shown in the text below the button with a prefix, and whenever the button will be clicked we will go back to splash(sounds absurd though).

```
@Composable
fun HomePage(
    inputValue: State<String> = rememberStringState("inputValue"),
    labelValue: State<String> = rememberStringState("labelValue"),
    notifier: NotificationService = rememberNotifier()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        TextField(
            value = inputValue.value,
            onValueChange = {
                notifier.notify("input_value",it)
            }
        )
        Text(labelValue.value)
        Button(
            onClick = {
                notifier.notify("go_back)
            }
        ) {
            Text("Go Back")
        }
    }
}
```

The followings are bit confusional.

```
inputValue: State<String> = rememberStringState("inputValue"),
labelValue: State<String> = rememberStringState("labelValue"),
notifier: NotificationService = rememberNotifier()
```

In J3 UI and ViewModels are decoupled but connected. UI will not know which viewModel is connected with it and viewModel also will not know which UI is connected with it. But data should come from viewModel and events should be notified to viewModel. J3 has introduced `WirelessViewModel` concept. Data and notification can be transferred by id only.
Where we take data as `inputValue: String = viewModel.inputValue.value` there we have to use `inputValue: State<String> = rememberStringState(id_of_your_choice)`. Id can be anything.
Since viewModel is not directly accessable event notification also need to be transferred by id. Here we have to use `NotifiactionService` to notify event.

Let's see the HomePageViewModel:

```
class HomeViewModel: WirelessViewModel(){
    private val inputValue = mutableStateOf("")
    private val labelValue = mutableStateOf("")

    override fun onBack() {

    }

    override fun onStart() {
        setStatusBarColor(Color.Red,false)
    }

    override fun onNotification(id: Any?, arg: Any?) {
        when(id){
            "go_back"->navigate {
                popBackStack()
            }
            "input_value"->{
                inputValue.value = arg as String
                labelValue.value = "Result = "+inputValue.value
            }
        }
    }

    init {
        controller.resolver.addAll(
            MyDataIds.inputValue to inputValue,
            MyDataIds.labelValue to labelValue,
        )
    }
}
```

In viewModel we have `onNotification` where you will get all the events notified with `id` and corresponding value(if send). Value is optional though.

## Why we need WirelessViewModel?

In Jetpack Compose when we use ViewModel nested or deep ui components are also coupled with viewModel or values have to be pushed down to the deepest ui component. So to reuse ui components across different pages with different viewModels it is required to have some abstraction mechanism. With `WirelessViewModel` ui components are not coupled to specific viewModel also only those ui components have to take data which really need it instead of all in the chain.

```
@Composable
fun RootUI(){
	Column(){
		ChildUI()
	}
}

@Composable
fun ChildUI(){
	Row(){
		GrandChildUI()
		Text("Another Grand Child"))
	}
}

@Composable
fun GrandChild(
	inputValue: State<String> = rememberStringState("inputValue")
){
	Box(){
		Text("Hello World")
		Text(inputValue.value)
	}
}
```

From the above example you can see data need not be passed in chain arguments. Only those components will take data which really need it, others need not to know about data source and consumer.

Same goes to event notification. From any ui depth any ui component can send event notification.

Data can be registered in viewModel like below:

```
controller.resolver.addAll(
    "input_value" to inputValue,
    "label_value" to labelValue,
)
```

If you don't register your data but tried to consume it the app will crash for sure. It is by design.

In general we do define two variable per data.

```
private val _inputValue = mutableStateOf("")
val inputValue: State<String> = _inputValue
```

We do these to use the mutableStateOf data without having the risk to be edited from unwanted places. But in WirelessViewModel you don't need to have two variable per data, because it is already being passed as State<T> which is immutable.

So all your basic needs will be fulfilled by WirelessViewModel. Along with you will have navigation, toast, etc.

<!--stackedit_data:
eyJoaXN0b3J5IjpbODUzNTMwNDU4LDU4MjAyODI0MiwtMzI3OT
cyNTc5LC0yMzg1NTQ1ODUsMzQ1OTc2MDY3LDcyMDI0MDIzNiwt
MTA1MTY1ODM0OSw1MTg0OTIyMzYsMTI1MjQ2ODAyNiwtMTIyOT
kyODQwNiwtMTgwNzgyNjU4OCw3MjI5MTg0NzQsLTE4NzMwNjA2
MjcsLTM5NTY1MDQwNiwtMTkzNjk1NTM1MSwtMjA4NDk2NzU1NS
wtNzkzMDk2NzNdfQ==
-->