# J3
[![](https://jitpack.io/v/Debdutta-Panda/J3.svg)](https://jitpack.io/#Debdutta-Panda/J3)

Jetpack Compose Material 3 Architectural Utility Library

## Installation
Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency
```groovy
dependencies {
	implementation 'com.github.Debdutta-Panda:J3:<version>'
}
```

# What is J3?

J3 is a architectural utility library which enforch MVVM in jetpack compose project along with providing lots of utilities like navigation from viewModel, toast showing, permission checking and requesting, camera preview obtaining, pixel perfect ui developing and many more.

# WirelessViewModel: Taming the UI Complexity

## What is WirelessViewModel?

The WirelessViewModel is a powerful abstraction within our library that addresses a common challenge in Jetpack Compose applications – ViewModel bloating.

## Why is it Needed?

In Compose-based applications, ViewModels play a crucial role in managing UI-related data and logic. However, as the UI grows in complexity, so does the ViewModel, often resulting in a tight coupling between Composables and specific ViewModel implementations. This can make the codebase less modular and harder to maintain.

## The Solution: Decoupled Data Management

Our WirelessViewModel offers a solution to this problem by decoupling Composables from specific ViewModel implementations. Instead of directly accessing ViewModel properties, Composables can interact with data through a flexible notification system. This approach allows you to:

- **Decouple Composables**: Composables become more modular and reusable as they're no longer tightly bound to specific ViewModels.

- **Flexibility**: Change the source of data or business logic without impacting Composables.

- **Reduce ViewModel Bloating**: Keep ViewModels focused on their core responsibilities.

- **Shared Data**: Easily share data and trigger actions between different parts of your application.

## Simplifying UI Development

With WirelessViewModel, you can streamline UI development, making your codebase more maintainable and adaptable as your application evolves.

## Example

Let's create a simple wirelessViewModel

```kt
class HomeViewModel: WirelessViewModelInterface, ViewModel(){
    private val _statusBarColor = mutableStateOf<com.debduttapanda.j3lib.StatusBarColor?>(null)
    override val softInputMode = mutableStateOf(com.debduttapanda.j3lib.SoftInputMode.adjustNothing)
    override val resolver = com.debduttapanda.j3lib.Resolver()
    override val notifier = com.debduttapanda.j3lib.NotificationService { id, arg ->
        when (id) {
            "${DataIds.back}home" -> {
                navigation.scope { navHostController, lifecycleOwner, activityService ->
                    navHostController.popBackStack()
                }
            }
        }
    }
    override val navigation = com.debduttapanda.j3lib.Navigation()
    override val permissionHandler = com.debduttapanda.j3lib.PermissionHandler()
    override val resultingActivityHandler = com.debduttapanda.j3lib.ResultingActivityHandler()
    init {
        resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
        )
        _statusBarColor.value = com.debduttapanda.j3lib.StatusBarColor(
            color = Color.Red,
            darkIcons = true
        )
    }
}
```
This is minimal bare bone wirelessViewModel.
It has lots of functionality will discuss later.
First of all let's use it.
**Create App Composable**

```kt
@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Routes.splash.full
    ) {
        MyScreen(
            {
                viewModel<HomeViewModel>()//provide your wirelessViewModel
            },
            navController = navController,
            route = Routes.home.full// route name in string, you can use simple "home" etc.
        ) {
            HomePage()// your composable
        }
    }
}
```

**Place the App Composable**

```kt
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
```
**Design your Composable page**

```kt
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
```

Any *data from viewModel* and *event notification towards viewModel* will be done by id.
**That is how J3 abstracted the viewModel.**
ViewModel will declare data with specific id(s) and will subscribe event through specific id(s).
**Declaring data in viewModel with id**

```kt
resolver.addAll(
    DataIds.statusBarColor to _statusBarColor,
    MyDataIds.inputValue to inputValue,
    MyDataIds.labelValue to labelValue,
)
``` 

**Accessing data in composable**

```kt
@Composable
fun HomePage(
    inputValue: String = stringState(key = MyDataIds.inputValue).value,
    labelValue: String = stringState(key = MyDataIds.labelValue).value,
    ...
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text("Home")
        TextField(
            value = inputValue,
            onValueChange = {}
        )
        Text(labelValue)
        ...
    }
}
```

Notice the id should be same, otherwise the app will crash **surely**.

This way the data in viewModel is always *private* and *immutable*, but still you can access in a specific scope. It reduces the effort of creating two variable, one private mutableStateOf and one public State<T>.

**Subscribing event in viewModel**

```kt
override val notifier = NotificationService { id, arg ->
    when (id) {
        "${DataIds.back}home" -> {
            navigation.scope { navHostController, lifecycleOwner, activityService ->
                navHostController.popBackStack()
            }
        }
        MyDataIds.inputValue->{
            inputValue.value = arg as String
            labelValue.value = "Result = "+inputValue.value
        }
        MyDataIds.goBack->{
            navigation.scope { navHostController, lifecycleOwner, activityService ->
                navHostController.popBackStack()
            }
        }
    }
}
```

**Notifying event from composable**

```kt
@Composable
fun HomePage(
    inputValue: String = stringState(key = MyDataIds.inputValue).value,
    notifier: NotificationService = com.debduttapanda.j3lib.notifier(),
    ...
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
	    ...
        TextField(
            value = inputValue,
            onValueChange = {
                notifier.notify(MyDataIds.inputValue,it)
            }
        )
        ...
    }
}
```

**WirelessViewModel summary**
1. Declare data in viewModel with id
2. Access data in composable with same id
3. Notify event from composable with data by id
4. Subscribe event with data in viewModel with same id 
<!--stackedit_data:
eyJoaXN0b3J5IjpbNjUwOTczNDg1LC0yMDg0OTY3NTU1LC03OT
MwOTY3M119
-->