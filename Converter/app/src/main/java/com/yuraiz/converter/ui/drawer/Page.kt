package com.yuraiz.converter.ui.drawer

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Composable Widget with icon and name.
 */
class Page(
    val icon: ImageVector,
    val name: String,
    private val content: @Composable () -> Unit
) {
    @SuppressLint("ComposableNaming")
    @Composable
    operator fun invoke() = content()
}