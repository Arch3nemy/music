package com.alacrity.music.main.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alacrity.music.R
import com.alacrity.music.core.theme.*
import com.alacrity.music.entity.Audio
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun PlayPauseButton(isPlaying: Boolean, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor = TransparentWhite),
        modifier = Modifier.size(75.dp),
        shape = CircleShape,
        elevation = null
    ) {
        Icon(
            if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            null, tint = MusicTheme.colors.textAndIcons
        )
    }
}

@Composable
fun PlayerViewIconButton(icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Transparent),
        modifier = Modifier.padding(top = 14.dp),
        elevation = null
    ) {
        Icon(
            imageVector = icon, null, tint = MusicTheme.colors.textAndIcons,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun ToolbarIconButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Transparent),
        modifier = Modifier.padding(top = 14.dp),
        shape = CircleShape,
        elevation = null
    ) {
        Column {
            Icon(
                imageVector = icon, null, tint = MusicTheme.colors.textAndIcons,
                modifier = Modifier.size(28.dp).align(CenterHorizontally)
            )
            TextView(text = description, modifier = Modifier.align(CenterHorizontally))
        }

    }
}

@Composable
fun TextView(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    style: androidx.compose.ui.text.TextStyle = MusicTypography.h1
) {
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(color = MusicTheme.colors.textAndIcons),
        textAlign = textAlign,
        maxLines = 2
    )
}

fun Modifier.diagonalGradientTint(
    colors: List<Color>,
    blendMode: BlendMode
) = drawWithContent {
    drawContent()
    drawRect(
        brush = Brush.linearGradient(colors),
        blendMode = blendMode
    )
}

fun Modifier.offsetGradientBackground(
    colors: List<Color>,
    width: Float,
    offset: Float = 0f
) = background(
    Brush.horizontalGradient(
        colors,
        startX = -offset,
        endX = width - offset,
        tileMode = TileMode.Mirror
    )
)

@Composable
fun CircularImage(id: Any?, modifier: Modifier = Modifier) {
    GlideImage(
        imageModel = id as Uri,
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .border(1.dp, Color.Gray, CircleShape),
        error = painterResource(id = R.mipmap.ic_album_default_round),
        shimmerParams = ShimmerParams(
            baseColor = MaterialTheme.colors.background,
            highlightColor = Gray,
            durationMillis = 350,
            dropOff = 0.65f,
            tilt = 20f
        )
    )
}

@Composable
fun AudioView(item: Audio, isPlaying: Boolean, onClick: (Audio) -> Unit) {
    val color = if (isPlaying) MusicTheme.colors.musicItemBackgroundSelected else MusicTheme.colors.musicItemBackground
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick(item) },
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            CircularImage(
                id = item.albumArt as Uri,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 5.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ) {
                TextView(text = item.title, modifier = Modifier.padding(top = 10.dp))
                TextView(text = item.artist, modifier = Modifier.padding(top = 10.dp))
            }
        }

    }
}

@Composable
fun InnerFab(isPlaying: Boolean, onShuffle: () -> Unit, onClickPlayPause: () -> Unit) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .background(MusicTheme.colors.primary)
    ) {
        IconButton(
            onClick = { onShuffle() }
        ) {
            Icon(
                Icons.Filled.Shuffle, null,
                tint = MusicTheme.colors.textAndIcons
            )
        }

        IconButton(
            onClick = { onClickPlayPause() }
        ) {
            Icon(
                if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                null,
                tint = MusicTheme.colors.textAndIcons
            )
        }
    }
}

@Composable
fun InnerSongViewImageBackground(audio: Audio, modifier: Modifier) {
    GlideImage(
        imageModel = audio.albumArt as Uri,
        modifier = modifier
            // .clip(RoundedCornerShape(corner = CornerSize(10)))
            .blur(radius = 100.dp),
        contentScale = ContentScale.FillBounds,
        //alpha = alpha,
        error = painterResource(id = R.drawable.music_image_hd),
        shimmerParams = ShimmerParams(
            baseColor = MaterialTheme.colors.background,
            highlightColor = Gray,
            durationMillis = 350,
            dropOff = 0.65f,
            tilt = 20f
        )
    )
}

@Composable
fun RestartButton(modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Transparent),
        modifier = modifier.padding(top = 14.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TextView(text = "Tap to Restart Music", modifier = Modifier.align(CenterHorizontally))
            Spacer(modifier = Modifier.height(10.dp))
            Icon(
                imageVector = Icons.Filled.RestartAlt, null, tint = MusicTheme.colors.textAndIcons,
                modifier = Modifier
                    .size(50.dp)
                    .align(CenterHorizontally)
            )
        }

    }
}
