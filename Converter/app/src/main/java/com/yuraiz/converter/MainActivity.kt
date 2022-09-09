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

import androidx.compose.material.icons.Icons.Outlined as Icons
import com.yuraiz.converter.ui.views.ConverterView
import kotlin.math.pow

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
                    Content()
                }
            }
        }
    }
}

@Composable
fun Content() {
    Drawer(
        volumePage(),
        lengthPage(),
        dataPage(),
    )
}

fun volumePage() = Page(
    Icons.SportsBar,
    "Volume"
) {
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

fun lengthPage() = Page(
    Icons.SocialDistance,
    "Distance"
) {
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

fun dataPage() = Page(
    Icons.Save,
    "Data"
) {
    ConverterView(
        "bit" to 1.0,
        "byte" to 8.0,

        // SI prefixes
        "kilobyte" to 8e3,
        "megabyte" to 8e6,
        "gigabyte" to 8e9,
        "terabyte" to 8e12,
        "petabyte" to 8e15,
        "exabyte" to 8e18,

        // Binary prefixes
        "kibibyte" to 8.0 * 2.0.pow(10),
        "mebibyte" to 8.0 * 2.0.pow(20),
        "gibibyte" to 8.0 * 2.0.pow(30),
        "tebibyte" to 8.0 * 2.0.pow(40),
        "pebibyte" to 8.0 * 2.0.pow(50),
        "exbibyte" to 8.0 * 2.0.pow(60),
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Content()
    }
}
