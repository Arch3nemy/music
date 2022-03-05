package com.alacrity.music.core

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.alacrity.music.core.theme.MusicTheme
import com.alacrity.music.main.MainViewModel
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun MusicApp(
    context: Context,
    viewModel: MainViewModel
) {
    MusicTheme {
        ProvideWindowInsets {
            Navigation(context = context, viewModel = viewModel)
        }
    }

}
