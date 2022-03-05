package com.alacrity.music.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = MusicColors(
    primary = Purple,
    musicItemBackground = LightYellow,
    musicItemBackgroundSelected = Orange,
    uiBackground = Color.White,
    gradient6_1 = listOf(Yellow, Orange, Color.Green, Color.White, Color.Gray),
    textAndIcons = Color.Black,
    isDark = false
)

private val DarkColorPalette = MusicColors(
    primary = Purple,
    musicItemBackground = DarkGray,
    musicItemBackgroundSelected = DarkOrange,
    uiBackground = Color.Black,
    gradient6_1 = listOf(Violette, Purple, Orange, LightYellow, Blue),
    textAndIcons = Color.White,
    isDark = true
)

@Composable
fun MusicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    val sysUiController = rememberSystemUiController()
    SideEffect {
        sysUiController.setSystemBarsColor(
            color = colors.uiBackground.copy()
        )
    }

    ProvideMusicColors(colors) {
        MaterialTheme(
            colors = debugColors(darkTheme),
            typography = MusicTypography,
            shapes = MusicShapes,
            content = content
        )
    }
}

object MusicTheme {
    val colors: MusicColors
        @Composable
        get() = LocalMusicColors.current
}

/**
 * Music custom Color Palette
 */
@Stable
class MusicColors(
    primary: Color,
    gradient6_1: List<Color>,
    uiBackground: Color,
    textAndIcons: Color,
    musicItemBackground: Color,
    musicItemBackgroundSelected: Color,
    isDark: Boolean
) {
    var primary by mutableStateOf(primary)
        private set
    var musicItemBackground by mutableStateOf(musicItemBackground)
        private set
    var gradient6_1 by mutableStateOf(gradient6_1)
        private set
    var textAndIcons by mutableStateOf(textAndIcons)
        private set
    var uiBackground by mutableStateOf(uiBackground)
        private set
    var musicItemBackgroundSelected by mutableStateOf(musicItemBackgroundSelected)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: MusicColors) {
        primary = other.primary
        gradient6_1 = other.gradient6_1
        uiBackground = other.uiBackground
        textAndIcons = other.textAndIcons
        musicItemBackground = other.musicItemBackground
        musicItemBackgroundSelected = other.musicItemBackgroundSelected
        isDark = other.isDark
    }

    fun copy(): MusicColors = MusicColors(
        primary = primary,
        gradient6_1 = gradient6_1,
        uiBackground = uiBackground,
        textAndIcons = textAndIcons,
        musicItemBackground = musicItemBackground,
        musicItemBackgroundSelected = musicItemBackgroundSelected,
        isDark = isDark,
    )
}

@Composable
fun ProvideMusicColors(
    colors: MusicColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalMusicColors provides colorPalette, content = content)
}

private val LocalMusicColors = staticCompositionLocalOf<MusicColors> {
    error("No MusicColorPalette provided")
}

/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colors] in preference to [MusicTheme.colors].
 */
fun debugColors(
    darkTheme: Boolean,
    debugColor: Color = Color.Magenta
) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = Color.White,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = !darkTheme
)



