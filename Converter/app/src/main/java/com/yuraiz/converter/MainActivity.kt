package com.yuraiz.converter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yuraiz.converter.ui.theme.ConverterTheme
import com.yuraiz.converter.ui.drawer.Drawer
import com.yuraiz.converter.ui.drawer.Page
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Drawer(
                        Page(Outlined.AccountBox, "Hello") {
                            Text("Hello")
                        },
                        Page(Outlined.Face, "Bye") {
                            Text("Bye")
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun Content() {
    val input = remember { mutableStateOf("") }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
        contentAlignment = Alignment.TopStart

    ) {
        val width = 600.dp
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.surface)
        ) {
            Column(
                modifier = Modifier
                    .width(width)
            ) {

                Text(input.value)
                Spacer(modifier = Modifier.height(16.dp))
                Keypad(input)
            }
        }
    }
}

@Composable
fun Keypad(input: MutableState<String>) {
    val callback = { text: String ->
        handleButtonClick(text, input)
    }

    @Composable
    fun NumKeypadRow(
        texts: List<String>,
        callback: (text: String) -> Any
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            texts.forEach {
                Button(
                    modifier = Modifier
                        .padding(2.dp)
                        .height(120.dp)
                        .clip(CircleShape)
                        .weight(1f),

                    onClick = { callback(it) }
                ) {
                    Text(it, style = MaterialTheme.typography.h3)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(16.dp)

    ) {
        NumKeypadRow(
            listOf("7", "8", "9"),
            callback
        )
        NumKeypadRow(
            listOf("4", "5", "6"),
            callback
        )
        NumKeypadRow(
            listOf("1", "2", "3"),
            callback
        )
        NumKeypadRow(
            listOf(".", "0", "DEL"),
            callback
        )
    }
}

@Composable
fun NumKeypadRow(
    texts: List<String>,
    callback: (text: String) -> Any
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        texts.forEach {
            Button(
                modifier = Modifier
                    .padding(2.dp)
                    .height(120.dp)
                    .clip(CircleShape)
                    .weight(1f),

                onClick = { callback(it) }
            ) {
                Text(it, style = MaterialTheme.typography.h3)
            }
        }
    }
}


fun handleButtonClick(
    buttonText: String,
    inputTextView: MutableState<String>,
) {
    when (buttonText) {
        "." -> if ('.' !in inputTextView.value) {
            inputTextView.value += '.'
        }
        "DEL" -> inputTextView.value = inputTextView.value.dropLast(1)

        else -> inputTextView.value += buttonText
    }
}

//@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Content()
    }
}
