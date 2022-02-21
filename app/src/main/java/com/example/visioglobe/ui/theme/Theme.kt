package com.example.visioglobe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object VisioGlobeAppTheme {
    val dims: AppDims
        @Composable
        @ReadOnlyComposable
        get() = LocalAppDims.current
}

private val DarkColorPalette = darkColors(
    primary = Orange,
    primaryVariant = OrangeLight,
    secondary = Grey,
    background = Black,
    surface = Black,
    error = Red,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    onError = White
)

private val LightColorPalette = lightColors(
    primary = Orange,
    primaryVariant = OrangeLight,
    secondary = GreyDark,
    background = White,
    surface = White,
    error = Red,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
    onError = White
)

private val LocalAppDims = staticCompositionLocalOf {
    defaultAppDims()
}

@Composable
fun VisioGlobeAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dims: AppDims = appDims(),
    content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(
        LocalAppDims provides dims
    ) {
        MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
    if (darkTheme) {
        rememberSystemUiController().setStatusBarColor(
            color = Color.Black,
            darkIcons = false
        )
        rememberSystemUiController().setNavigationBarColor(
            color = Color.Black,
            darkIcons = false
        )
    } else {
        rememberSystemUiController().setStatusBarColor(
            color = MaterialTheme.colors.surface,
            darkIcons = true
        )
        rememberSystemUiController().setNavigationBarColor(
            color = MaterialTheme.colors.surface,
            darkIcons = true
        )
    }
}