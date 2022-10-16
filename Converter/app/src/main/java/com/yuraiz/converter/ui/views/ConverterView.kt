package com.yuraiz.converter.ui.views

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons.Outlined as Icons
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import kotlinx.coroutines.delay

fun Double.beautyFormat(): String {
    var string = String.format("%.10f", this)
    string.dropWhile { it == '0' }
    if ('.' in string) {
        string =
            string.dropLastWhile { it == '0' }.dropLastWhile { it == '.' }
    }
    return string
}

@Composable
fun ConverterView(options: Map<String, Double>) {
    val keypad = MyKeypad()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        val rowCount = minOf(3, options.size)

        var number by remember { mutableStateOf(0.0) }

        val selected by remember {
            mutableStateOf(
                options.keys.take(rowCount).toMutableList()
            )
        }

        CompositionLocalProvider(keypad.provide()) {
            Column {
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
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(selected[index])
                                Icon(Icons.ArrowDropDown, null)
                            }

                            DropdownMenu(
                                expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                for (option in options.keys.minus(selected.toSet())) {
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

                        var focused by rememberSaveable {
                            mutableStateOf(false)
                        }

                        var text by if (focused) {
                            remember {
                                mutableStateOf("")
                            }
                        } else {
                            remember(number, factor) {
                                mutableStateOf((number / factor).beautyFormat())
                            }
                        }

                        if (focused) {
                            number = (text.toDoubleOrNull() ?: 1.0) * factor
                        }

                        val focusRequester = remember {
                            FocusRequester()
                        }

                        val clipboardManager = LocalClipboardManager.current

                        val copyButton = @Composable {
                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(text))
                            }) {
                                Icon(Icons.ContentCopy, "copy")
                            }
                        }

                        OutlinedTextField(
                            value = text,
                            singleLine = true,
                            onValueChange = {
                                number = (it.toDoubleOrNull() ?: 1.0) * factor
                                text = it
                            },
                            modifier = Modifier
                                .focusRequester(focusRequester)
//                                .selectable(false) {}
                                .onFocusChanged { focused = it.isFocused },
                            placeholder = { Text("1") },
                            trailingIcon = copyButton
                        )

                        if (index == 0) {
                            LaunchedEffect(Unit) {
                                focusRequester.requestFocus()
                            }
                        }
                    }
                }
            }
        }
        keypad.Show()
    }
}

class MyKeypad : PlatformTextInputService {

    private var onEditCommand: ((List<EditCommand>) -> Unit) = {}
    private var textFieldValue: TextFieldValue? = null
    private var enabledState = mutableStateOf(false)

    override fun startInput(
        value: TextFieldValue,
        imeOptions: ImeOptions,
        onEditCommand: (List<EditCommand>) -> Unit,
        onImeActionPerformed: (ImeAction) -> Unit
    ) {
        this.onEditCommand = onEditCommand
        this.textFieldValue = value
        this.enabledState.value = true
    }

    override fun stopInput() {
        onEditCommand = {}
    }

    override fun updateState(oldValue: TextFieldValue?, newValue: TextFieldValue) {
        this.textFieldValue = newValue
        this.enabledState.value = newValue.text.length < 18
    }

    override fun hideSoftwareKeyboard() = Unit
    override fun showSoftwareKeyboard() = Unit
    override fun notifyFocusedRect(rect: Rect) = Unit

    fun provide() = LocalTextInputService provides TextInputService(this)

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Show() {
        val enabled by remember {
            this.enabledState
        }

        val callback = closure@{ text: String ->
            val commands = when (text) {
                "." -> {
                    var value = textFieldValue?.text
                    when {
                        value.isNullOrEmpty() -> listOf(CommitTextCommand("0.", 1))
                        value.contains(".") -> {

                            val selection = textFieldValue!!.selection

                            var cursorPos = selection.start

                            value = value.removeRange(selection.start, selection.end)

                            val resultText = StringBuilder(value.replace('.', 'D')).apply {
                                insert(cursorPos, '.')
                                val index = indexOf('D')
                                if (index != -1) {
                                    deleteCharAt(index)
                                }
                                if (startsWith(".", 0)) {
                                    insert(0, '0')
                                }
                            }.toString()

                            listOf(
                                DeleteAllCommand(),
                                CommitTextCommand(resultText,0),
                                MoveCursorCommand(resultText.indexOf('.') + 1)
                            )
                        }
                        else -> listOf(CommitTextCommand(text, 1))
                    }
                }
                "DEL" -> listOf(BackspaceCommand())
                else -> listOf(
                    CommitTextCommand(text, 1))
            }

            onEditCommand(commands)
        }

        val buttonNames = listOf(
            "7", "8", "9",
            "4", "5", "6",
            "1", "2", "3",
            ".", "0", "DEL"
        )

        val numKeypadRow = @Composable { texts: List<String> ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                texts.forEach {
                    var pressed by remember {
                        mutableStateOf(false)
                    }

                    Button(
                        enabled = enabled || it == "DEL",
                        modifier = Modifier
                            .padding(8.dp)
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

        Column {
            buttonNames
                .chunked(3)
                .forEach { numKeypadRow(it) }
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

@Composable
fun ConverterView(vararg options: Pair<String, Double>) =
    ConverterView(mapOf(*options))

@Preview(showBackground = true)
@Composable
private fun ConverterPreview() {
    MaterialTheme {
        ConverterView(
            // Metric
            "meter" to 1.0,
            "picometer" to 1e-6,
            "millimeter" to 1e-3,
            "centimeter" to 1e-2,
            "kilometer" to 1e3,

            // Imperial
            "inch" to 0.0254,
            "foot" to 0.3048,
            "yard" to 0.9144,
            "mile" to 1609.344,

            // Marine
            "nautical mile" to 1852.0,
        )
    }
}