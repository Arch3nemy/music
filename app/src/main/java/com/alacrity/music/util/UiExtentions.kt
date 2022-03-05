package com.alacrity.music.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alacrity.music.core.MainActivity
import com.alacrity.music.main.ui.PlayingState
import kotlin.system.exitProcess


//Returns pair of screen width and height
@Composable
inline fun <reified T> getScreenSize(): Pair<T, T> {
    val configuration = LocalConfiguration.current
    with(configuration) {
        return when (T::class) {
            Int::class -> Pair(screenWidthDp as T, screenHeightDp as T)
            Dp::class -> Pair(screenWidthDp.dp as T, screenHeightDp.dp as T)
            else -> Pair(
                with(LocalDensity.current) { screenWidthDp.dp.toPx() } as T,
                with(LocalDensity.current) { screenHeightDp.dp.toPx() } as T)
        }
    }
}

fun PlayingState.getPlayingIndex(): Int = when (this) {
    PlayingState.InitialState -> -1
    is PlayingState.Paused -> index
    is PlayingState.Playing -> index
}

fun restartApp(context: Context) {
    val mStartActivity = Intent(context, MainActivity::class.java)
    val mPendingIntentId = 123456
    val mPendingIntent = PendingIntent.getActivity(
        context,
        mPendingIntentId,
        mStartActivity,
        PendingIntent.FLAG_CANCEL_CURRENT
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), mPendingIntent)
    exitProcess(0)
}