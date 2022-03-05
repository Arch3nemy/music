package com.alacrity.music.core

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alacrity.music.entity.Audio
import com.alacrity.music.main.MainViewModel
import com.alacrity.music.main.ui.MainScreen
import com.alacrity.music.utils.fromJson
import com.alacrity.music.utils.logThis

val testScreenArgs = listOf(
    navArgument("item") {
        type = NavType.StringType
    }
)

@Composable
fun Navigation(context: Context, viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(context = context, viewModel = viewModel, navController = navController)
        }
        composable(route = Screen.ViewAudioScreen.route + "/{name}", arguments = testScreenArgs) { entry ->
            TestScreen(arg = entry.arguments?.getString("name"))
        }
    }
}

/**
 * How to navigate with navController
 * navController.navigate(Screen.TestScreen.withArgs(args))
 */

@Composable
fun TestScreen(arg: String?) {
    arg?.fromJson(Audio::class.java).logThis("Given argument")
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Hello there from test screen")
    }
}

@Composable
fun TestScreen(navController: NavController, arg: Audio) {

}