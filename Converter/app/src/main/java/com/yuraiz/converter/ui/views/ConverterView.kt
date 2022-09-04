package com.yuraiz.converter.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.tooling.preview.Preview
import com.yuraiz.converter.handleButtonClick
import java.lang.NumberFormatException

@Composable
fun ConverterViewOld(options: Map<String, Double>) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
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
                            .align(Alignment.BottomStart)
                            .fillMaxSize()
                    ) {
                        Text(selected[index])
                        Icon(Icons.Outlined.ArrowDropDown, null)
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

                var error by remember {
                    mutableStateOf(false)
                }

                fun Double.beautyFormat(): String {
                    var string = String.format("%.10f", this)
                    if ('.' in string) {
                        string = string.dropLastWhile { it == '0' }.dropLastWhile { it == '.' }
                    }
                    return string
                }

                var text by if (focused) {
                    remember(factor) {
                        mutableStateOf((number / factor).beautyFormat())
                    }
                } else {
                    remember(number, factor) {
                        mutableStateOf((number / factor).beautyFormat())
                    }
                }

                OutlinedTextField(
                    value = text,
                    singleLine = true,
                    onValueChange = {
                        try {
                            number = it.toDouble() * factor
                            error = false
                        } catch (format: NumberFormatException) {
                            error = true
                        }

                        text = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.onFocusChanged {
                        focused = it.isFocused
                    },
                    isError = error
                )
            }

        }
    }
}



@Composable
fun ConverterView(options: Map<String, Double>) {
    val input = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
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
                            .align(Alignment.BottomStart)
                            .fillMaxSize()
                    ) {
                        Text(selected[index])
                        Icon(Icons.Outlined.ArrowDropDown, null)
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

                var error by remember {
                    mutableStateOf(false)
                }

                fun Double.beautyFormat(): String {
                    var string = String.format("%.10f", this)
                    if ('.' in string) {
                        string = string.dropLastWhile { it == '0' }.dropLastWhile { it == '.' }
                    }
                    return string
                }

                var text by if (focused) {
                    remember(factor) {
                        mutableStateOf((number / factor).beautyFormat())
                    }
                } else {
                    remember(number, factor) {
                        mutableStateOf((number / factor).beautyFormat())
                    }
                }

                OutlinedTextField(
                    value = text,
                    singleLine = true,
                    onValueChange = {
                        try {
                            number = it.toDouble() * factor
                            error = false
                        } catch (format: NumberFormatException) {
                            error = true
                        }

                        text = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.onFocusChanged {
                        focused = it.isFocused
                    },
                    isError = error
                )
            }

        }
        Keypad(input)
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