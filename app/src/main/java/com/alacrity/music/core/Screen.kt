package com.alacrity.music.core

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object ViewAudioScreen: Screen("view_audio_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }

}
