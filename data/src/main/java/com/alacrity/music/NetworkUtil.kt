package com.alacrity.music

interface NetworkUtil {

    fun isOnline(): Boolean

    fun subscribeToConnectionChange(key: Any, onConnectionChanged: (Boolean) -> Unit)

    fun unsubscribe(key: Any)

}