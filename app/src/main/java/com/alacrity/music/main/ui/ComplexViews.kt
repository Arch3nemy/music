package com.alacrity.music.main.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.alacrity.music.core.theme.MusicTheme
import com.alacrity.music.core.theme.MusicTypography
import com.alacrity.music.core.theme.Transparent
import com.alacrity.music.entity.Audio
import com.alacrity.music.util.getPlayingIndex
import com.alacrity.music.util.getScreenSize
import com.alacrity.music.util.restartApp
import com.alacrity.music.utils.logThis
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import kotlin.math.absoluteValue

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(e: Throwable?, message: String, onRestartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MusicTheme.colors.uiBackground),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .align(CenterHorizontally)
                .width((getScreenSize<Float>().first / 2).dp)
                .background(MusicTheme.colors.primary),
            shape = RoundedCornerShape(3.dp)
        ) {
            TextView(
                text = "Occurred error: $e, with message: $message",
                style = MusicTypography.h2
            )
        }

        val activity = (LocalContext.current as? Activity)

        RestartButton(modifier = Modifier.align(CenterHorizontally)) {
            onRestartClick()
            activity?.let { restartApp(it) }
        }
    }
}

@Composable
fun MainScreen(
    audioFiles: List<Audio>,
    playbackState: PlayingState,
    onShuffle: () -> Unit,
    onClickPlayPause: () -> Unit,
    onItemClick: (Int, Audio) -> Unit,
    onClickFavourites: () -> Unit,
    onClickPlaylist: (LazyListState) -> Unit
) {
    val currentPlayingIndex = playbackState.getPlayingIndex()
    val listState = rememberLazyListState()

    CollapsingToolbarScaffold(
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            FunctionalToolbar(onClickShuffle = { onShuffle() },
                onClickPlaylist = { onClickPlaylist(listState) },
                onClickFavourites = { onClickFavourites() })
        },
        modifier = Modifier.background(MusicTheme.colors.uiBackground),
        scrollStrategy = ScrollStrategy.EnterAlways
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MusicTheme.colors.uiBackground)
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.align(
                    Alignment.TopCenter
                )
            ) {
                itemsIndexed(audioFiles) { index, audio ->
                    AudioView(item = audio, currentPlayingIndex == index) {
                        onItemClick(index, it)
                    }
                    Divider(color = if (MusicTheme.colors.isDark) Color.Black else Color.White)
                }
            }

            ControlFloatingActionButton(
                playbackState = playbackState,
                onShuffle = { onShuffle() },
                onClickPlayPause = { onClickPlayPause() },
                modifier = Modifier.align(
                    Alignment.BottomEnd
                )
            )
        }
    }
}

@Composable
fun FunctionalToolbar(
    onClickShuffle: () -> Unit,
    onClickFavourites: () -> Unit,
    onClickPlaylist: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(top = 20.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ToolbarIconButton(icon = Icons.Filled.Shuffle, description = "Shuffle") { onClickShuffle() }
        ToolbarIconButton(
            icon = Icons.Filled.Favorite,
            description = "Favourites"
        ) { onClickFavourites() }
        ToolbarIconButton(
            icon = Icons.Filled.PlaylistPlay,
            description = "Playlists"
        ) { onClickPlaylist() }
    }
}

@Composable
fun ControlFloatingActionButton(
    playbackState: PlayingState,
    onShuffle: () -> Unit,
    onClickPlayPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPlaying = playbackState.isPlaying
    ExtendedFloatingActionButton(
        text = { InnerFab(isPlaying, onShuffle, onClickPlayPause) },
        onClick = { },
        modifier = modifier
            .wrapContentSize()
            .padding(bottom = 10.dp, end = 3.dp),
        backgroundColor = MusicTheme.colors.primary
    )
}


@OptIn(ExperimentalPagerApi::class, dev.chrisbanes.snapper.ExperimentalSnapperApi::class)
@Composable
fun SongScreen(
    audioList: List<Audio>,
    shuffleState: Boolean,
    playbackState: PlayingState,
    onClickPrev: () -> Unit,
    onClickNext: () -> Unit,
    onClickPlayPause: () -> Unit,
    onClickPlaylist: () -> Unit,
    onClickMore: () -> Unit,
    onClickBack: () -> Unit,
    onToggleShuffle: () -> Unit
) {

    val index = playbackState.index
    val indexToPlay = if (index >= 0) index else 0
    val pagerState = rememberPagerState()
    var currentPage = indexToPlay

    LaunchedEffect(pagerState) {
        index.logThis("Index to play: $indexToPlay, index")
        pagerState.scrollToPage(currentPage)
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page > currentPage) onClickNext()
            else if (page < currentPage) onClickPrev()
            currentPage = page
        }
    }

    HorizontalPager(
        count = audioList.size,
        state = pagerState,
        itemSpacing = 50.dp,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        Card(
            shape = RectangleShape,
            modifier = Modifier
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    // We animate the scaleX + scaleY, between 85% and 100%
                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            InnerSongView(
                audio = audioList[indexToPlay],
                playbackState = playbackState,
                shuffleState = shuffleState,
                onClickPrev = { onClickPrev() },
                onClickNext = { onClickNext() },
                onClickPlayPause = { onClickPlayPause() },
                onClickPlaylist = { onClickPlaylist() },
                onClickMore = { onClickMore() },
                onClickBack = { onClickBack() },
                onToggleShuffle = { onToggleShuffle() }
            )
        }

    }
}

@Composable
fun InnerSongView(
    audio: Audio,
    playbackState: PlayingState,
    shuffleState: Boolean,
    onClickPrev: () -> Unit,
    onClickNext: () -> Unit,
    onClickPlayPause: () -> Unit,
    onClickPlaylist: () -> Unit,
    onClickBack: () -> Unit,
    onClickMore: () -> Unit,
    onToggleShuffle: () -> Unit,
) {

    InnerSongViewContent(
        audio = audio,
        modifier = Modifier.fillMaxSize(),
        playbackState = playbackState,
        shuffleState = shuffleState,
        onClickPrev = onClickPrev,
        onClickNext = onClickNext,
        onClickPlayPause = onClickPlayPause,
        onClickPlaylist = onClickPlaylist,
        onClickBack = onClickBack,
        onClickMore = onClickMore,
        onToggleShuffle = onToggleShuffle,
    )
    InnerSongViewImageBackground(
        audio = audio, modifier = Modifier
            .fillMaxSize()
            .alpha(0.73f)
    )
}


@Composable
fun PlayerView(
    playbackState: PlayingState,
    shuffleState: Boolean,
    onClickPrev: () -> Unit,
    onClickNext: () -> Unit,
    onClickPlayPause: () -> Unit,
    onClickPlaylist: () -> Unit,
    onToggleShuffle: () -> Unit,
    modifier: Modifier = Modifier
) {

    val isPlaying = when (playbackState) {
        PlayingState.InitialState -> false
        is PlayingState.Paused -> false
        is PlayingState.Playing -> true
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val shuffleIcon = if(shuffleState) Icons.Filled.ShuffleOn else Icons.Filled.Shuffle
        PlayerViewIconButton(icon = shuffleIcon) {
            onToggleShuffle()
        }
        PlayerViewIconButton(icon = Icons.Filled.SkipPrevious) {
            onClickPrev()
        }
        PlayPauseButton(isPlaying) {
            onClickPlayPause()
        }

        PlayerViewIconButton(icon = Icons.Filled.SkipNext) {
            onClickNext()
        }

        PlayerViewIconButton(icon = Icons.Filled.PlaylistPlay) {
            onClickPlaylist()
        }
    }
}


@Composable
fun InnerSongViewContent(
    audio: Audio, modifier: Modifier,
    playbackState: PlayingState,
    shuffleState: Boolean,
    onClickPrev: () -> Unit,
    onClickNext: () -> Unit,
    onClickPlayPause: () -> Unit,
    onClickPlaylist: () -> Unit,
    onClickBack: () -> Unit,
    onClickMore: () -> Unit,
    onToggleShuffle: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MusicTheme.colors.uiBackground),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TopSongViewBar(audio = audio,
                onClickBack = { onClickBack() },
                onClickMore = { onClickMore() })
        }



        PlayerView(
            playbackState = playbackState,
            shuffleState = shuffleState,
            onClickPrev = { onClickPrev() },
            onClickNext = { onClickNext() },
            onClickPlayPause = { onClickPlayPause() },
            onClickPlaylist = { onClickPlaylist() },
            onToggleShuffle = { onToggleShuffle() },
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(bottom = 65.dp)
        )
    }
}

@Composable
fun SliderComposable(value: Float) {
    var sliderPosition by remember { mutableStateOf(value) }
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        valueRange = 0f..100f,
        onValueChangeFinished = {
            // launch some business logic update with the state you hold
            // viewModel.updateSelectedSliderValue(sliderPosition)
        },
        steps = 5,
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colors.secondary,
            activeTrackColor = MaterialTheme.colors.secondary
        )
    )
}

@Composable
fun TopSongViewBar(audio: Audio, onClickBack: () -> Unit, onClickMore: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Button(
            onClick = { onClickBack() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Transparent),
            modifier = Modifier.weight(1f),
            elevation = null
        ) {
            Icon(
                Icons.Filled.ArrowBackIosNew, null, tint = MusicTheme.colors.textAndIcons,
                modifier = Modifier.size(25.dp)
            )
        }

        Column(modifier = Modifier.weight(5f), horizontalAlignment = CenterHorizontally) {
            TextView(
                text = audio.title,
                textAlign = TextAlign.Center,
                style = MusicTypography.h2)

            TextView(
                text = audio.artist,
                textAlign = TextAlign.Center,
                style = MusicTypography.h3)
        }

        Button(
            onClick = { onClickMore() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Transparent),
            modifier = Modifier.weight(1f),
            elevation = null
        ) {
            Icon(
                Icons.Filled.MoreVert, null, tint = MusicTheme.colors.textAndIcons,
                modifier = Modifier.size(25.dp)
            )
        }

    }
}