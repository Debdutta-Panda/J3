package com.debduttapanda.j3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.debduttapanda.j3lib.ControlledById
import com.debduttapanda.j3lib.NotificationService
import com.debduttapanda.j3lib.SafeControlledById
import com.debduttapanda.j3lib.rememberBoolState
import com.debduttapanda.j3lib.rememberNotifier
import com.debduttapanda.j3lib.rememberStringState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    inputValue: State<String> = rememberStringState(MyDataIds.inputValue),
    labelValue: State<String> = rememberStringState(MyDataIds.labelValue),
    notifier: NotificationService = rememberNotifier()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text("Home")
        TextField(
            value = inputValue.value,
            onValueChange = {
                notifier.notify(MyDataIds.inputValue,it)
            }
        )
        Text(labelValue.value)
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
        ControlledById(MyDataIds.childCrontroller) {
            MyView()
        }

        SafeControlledById(id = MyDataIds.dialogController) {
            val notifier = rememberNotifier()
            val text = rememberStringState(id = MyDataIds.dialogText)
            val show = rememberBoolState(id = MyDataIds.showDialog)
            if(show.value){
                AlertDialog(
                    onDismissRequest = {
                        notifier.notify(0)
                    },
                    confirmButton = {
                        Button(onClick = {
                            notifier.notify(1)
                        }) {
                            Text("Ok")
                        }
                    },
                    text = {
                        Text(text.value)
                    }
                )
            }
        }
    }
}

@Composable
fun MyView(
    inputValue: State<String> = rememberStringState(MyDataIds.inputValue),
    labelValue: State<String> = rememberStringState(MyDataIds.labelValue),
    notifier: NotificationService = rememberNotifier()
){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text("Home")
        TextField(
            value = inputValue.value,
            onValueChange = {
                notifier.notify(MyDataIds.inputValue,it)
            }
        )
        Text(labelValue.value)
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