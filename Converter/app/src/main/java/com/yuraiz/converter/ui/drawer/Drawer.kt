package com.yuraiz.converter.ui.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuraiz.converter.ui.theme.ConverterTheme
import com.yuraiz.converter.ui.theme.Shapes
import kotlinx.coroutines.launch

import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Menu

import androidx.compose.material.icons.Icons.Outlined as Icons

/**
 * Simple drawer.
 *
 * example of usage:
 * ```
 * Drawer(
 *     Page(Icons.AccountBox,"Hello") {
 *         Text("Hello")
 *     },
 *     Page(Icons.Face, "Bye") {
 *         Text("Bye")
 *     }
 * )
 * ```
 */
@Composable
fun Drawer(vararg pages: Page) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var current by remember { mutableStateOf(pages.first()) }

    ModalDrawer(drawerState = drawerState,
        drawerContent = {
//            Spacer(Modifier.height(20.dp))
            for (page in pages) {
                DrawerRow(page.icon, page.name, current == page) {
                    current = page
                    scope.launch {
                        drawerState.close()
                    }
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    title = { Text(current.name) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Menu, contentDescription = null)
                        }
                    }
                )
                current()
            }
        }
    )
}

/**
 * Represents item in drawer.
 */
@Composable
private fun DrawerRow(icon: ImageVector, name: String, selected: Boolean, onClicked: () -> Unit) {
    val background = if (selected) {
        Modifier.background(MaterialTheme.colors.primary.copy(alpha = 0.12f))
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(Shapes.medium)
            .then(background)
            .clickable(onClick = onClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconTint = if (selected) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.onSurface
        }
        Icon(
            icon,
            tint = iconTint,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            contentDescription = null
        )
        Text(
            name,
            style = MaterialTheme.typography.body1,
            color = iconTint,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawerPreview() {
    ConverterTheme {
        Drawer(
            Page(Icons.AccountBox, "Hello") {
                Text("Hello")
            },
            Page(Icons.Face, "Bye") {
                Text("Bye")
            }
        )
    }
}