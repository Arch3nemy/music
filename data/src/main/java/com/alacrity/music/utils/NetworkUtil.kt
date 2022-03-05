package com.alacrity.music.utils

interface NetworkUtil {

    fun isOnline(): Boolean

    fun subscribeToConnectionChange(key: Any, onConnectionChanged: (Boolean) -> Unit)

    fun unsubscribe(key: Any)

}

