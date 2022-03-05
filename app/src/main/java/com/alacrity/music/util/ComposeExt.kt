package com.alacrity.music.util

import android.Manifest
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.alacrity.music.core.theme.Gray
import com.alacrity.music.core.theme.MusicTheme
import com.alacrity.music.core.theme.MusicTypography
import com.alacrity.music.entity.Audio
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandlePermissions(onPermissionsAccepted: () -> Unit, content: @Composable () -> Unit) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /*if(!permissionsState.allPermissionsGranted) {
            Text(
                text = "READ_EXTERNAL_STORAGE permission is needed for player to work properly",
                modifier = Modifier.align(CenterHorizontally)
            )
        } else {*/
        permissionsState.permissions.forEach { perm ->
            when (perm.permission) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    when {
                        perm.hasPermission -> {
                            onPermissionsAccepted(); content()
                        }
                        perm.shouldShowRationale -> {
                            Text(
                                text = "READ_EXTERNAL_STORAGE permission is needed for player to work properly",
                                modifier = Modifier.align(CenterHorizontally)
                            )
                        }
                        //}
                    }
                }
            }
        }
    }
}



fun customShape(screenWidth: Float, screenHeight: Float) = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(Rect(0f, 0f, screenWidth / 5, screenHeight))
    }
}

@Composable
fun ImageWithBackground(
    @DrawableRes backgroundDrawableResId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    audio: Audio,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    content: @Composable (Modifier) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        content(modifier.fillMaxSize())
    }
}

