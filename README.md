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
