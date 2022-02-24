package com.alacrity.music.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import com.alacrity.music.NetworkUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtilImpl @Inject constructor(private val context: Context) : NetworkUtil {

    private var isOnline = false

    private val networkConnectionCallbacks: MutableMap<String, ((Boolean) -> Unit)> = mutableMapOf()

    override fun isOnline(): Boolean = isOnline

    init {
        hasNetworkAvailable()
    }

    override fun subscribeToConnectionChange(key: Any, onConnectionChanged: (Boolean) -> Unit) {
        networkConnectionCallbacks[key.javaClass.simpleName] = onConnectionChanged
    }

    override fun unsubscribe(key: Any) {
        networkConnectionCallbacks.remove(key.javaClass.simpleName)
    }

    @Suppress("DEPRECATION")
    private fun hasNetworkAvailable() {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = manager.activeNetworkInfo
        isOnline = networkInfo?.isConnected == true
        notifyAllListener()
        manager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isOnline = true
                notifyAllListener()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isOnline = false
                notifyAllListener()
            }
        })
    }

    private fun notifyAllListener() {
        networkConnectionCallbacks.forEach { (_, onNetworkConnectionChanged) ->
            onNetworkConnectionChanged(isOnline)
        }
    }
}

