package com.yuraiz.converter.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.material.icons.Icons.Outlined as Icons
//import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.ui.graphics.Color


@Composable
fun ConverterView(options: Map<String, Double>) {
    val input = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        val rowCount = 3

        var number by remember { mutableStateOf(0.0) }

        val selected by remember {
            mutableStateOf(
                options.keys.take(rowCount).toMutableList()
            )
        }

        for (index in 0 until rowCount) {
            Row(
                modifier = Modifier
                    .height(72.dp)
                    .padding(8.dp)
            ) {

                val factor = options[selected[index]]!!

                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .fillMaxHeight()
                ) {

                    var expanded by remember { mutableStateOf(false) }

                    TextButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier

                            .fillMaxSize()
                    ) {
                        Text(
                            selected[index],
                            //                                textAlign = TextAlign.Left,
                        )
                        Icon(Icons.ArrowDropDown, null)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.keys.forEach { option ->
                            if (option in selected) {
                                return@forEach
                            }

                            DropdownMenuItem(
                                onClick = {
                                    selected[index] = option
                                    expanded = false
                                }
                            ) {
                                Text(text = option)
                            }
                        }
                    }
                }

                var focused by remember {
                    mutableStateOf(false)
                }

                fun Double.beautyFormat(): String {
                    var string = String.format("%.10f", this)
                    string.dropWhile { it == '0' }
                    if ('.' in string) {
                        string = string.dropLastWhile { it == '0' }.dropLastWhile { it == '.' }
                    }
                    return string
                }

                val text by if (focused) {
                    input
                } else {
                    remember(number, factor) {
                        mutableStateOf((number / factor).beautyFormat())
                    }
                }

                if (focused) {
                    number = (text.toDoubleOrNull() ?: 1.0) * factor
                }

                OutlinedTextField(
                    value = if (text != "") text else "1",
                    singleLine = true,
                    readOnly = true,
                    onValueChange = { },
                    modifier = Modifier.onFocusChanged {
                        focused = it.isFocused
                        if (focused) {
                            input.value = ""
                        }
                    },
                    textStyle = if (text != "") {
                        TextStyle.Default
                    } else {
                        TextStyle.Default.copy(color = Color.Gray)
                    }
                )
            }
        }
        Keypad(input)
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

@Composable
fun Keypad(input: MutableState<String>) {
    val callback = { text: String ->
        handleButtonClick(text, input)
        input.value = input.value.dropWhile { it == '0' }
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
                        .height(100.dp)
                        .clip(CircleShape)
                        .weight(1f),

                    onClick = { callback(it) }
                ) {
                    when (it) {
                        "DEL" -> Icon(
                            Icons.Backspace,
                            "delete",
                            modifier = Modifier.size(32.dp)
                        )
                        else -> Text(it, style = MaterialTheme.typography.h3)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        NumKeypadRow(
            listOf("7", "8", "9"),
            callback,
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
fun ConverterView(vararg options: Pair<String, Double>) =
    ConverterView(mapOf(*options))

@Preview(showBackground = true)
@Composable
private fun ConverterPreview() {
    MaterialTheme {
        ConverterView(
            "milliliter" to 1.0,
            "pinch" to 0.3080576,
            "liter" to 1000.0,
            "teaspoon" to 4.92892159375,
            "tablespoon" to 14.78676478125,
            "cup" to 236.588237,
            "pint" to 473.176473,
            "quart" to 946.352946,
            "gallon" to 3785.411784,
        )
    }
}