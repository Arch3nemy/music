package com.alacrity.music

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alacrity.music.ui.main.MainViewModel
import com.alacrity.music.util.getScreenSize
import com.alacrity.music.view_states.MainViewState

@Composable
fun MainScreen(
    context: Context,
    viewModel: MainViewModel,
) {

    val state by viewModel.viewState.collectAsState()

    MainWrapper(viewModel = viewModel) {
        when (state) {
            MainViewState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LinearProgressIndicator()
                }
            }
            is MainViewState.NoItems -> {

            }
            is MainViewState.Error -> {
                /* ShowErrorView */
            }

            is MainViewState.Refreshing -> {

            }
            else -> Unit
        }

        LaunchedEffect(key1 = state, block = {
            //viewModel.enterScreen()
        })

    }
}

@Composable
fun MainWrapper(viewModel: MainViewModel, content: @Composable () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    with(getScreenSize<Float>()) {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                /*AppDrawer(
                    closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } },
                    width = (first / 5 - 20).dp,
                    refresh = { viewModel.obtainEvent(HomeEvent.OnRefreshClicked) }
                )*/
            },
            drawerShape = customShape(first, second),
        ) {
            content()
        }
    }
}