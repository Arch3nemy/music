package com.alacrity.music.utils

import com.google.gson.Gson
import kotlin.reflect.KMutableProperty0

fun <T> runSafely(
    onSuccess: ((T) -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null,
    block: (() -> T)
) {
    try {
        val data = block.invoke()
        onSuccess?.invoke(data)
    } catch (e: Exception) {
        onError?.invoke(e)
    }
}

fun <T> runSafely(block: (() -> T)): Result<T> {
    return try {
        val data = block.invoke()
        Result.success(data)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun <T> KMutableProperty0<T>.runSafely(ifNotInitialized: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null, block: (() -> Unit)? = null) {
    if (!isLateinit) return
    try {
        get()
        block?.invoke()
    } catch (e: Exception) {
        if (e is UninitializedPropertyAccessException) {
            ifNotInitialized?.invoke()
            return
        }

        onError?.invoke(e)
    }
}


/**
 * @param valueIfInited value if given variable is initialized
 * @param valueIfNotInited value if given variable is not initialized
 * @return value depending on variable state
 */
fun <T, V> KMutableProperty0<V>.ifInited(valueIfInited: T, valueIfNotInited: T): T {
    try {
        get()
        return valueIfInited
    } catch (e: Exception) {
        if (e is UninitializedPropertyAccessException) {
            return valueIfNotInited
        }
    }
    return valueIfNotInited
}

fun <T> String.fromJson(type: Class<T>): T {
    return Gson().fromJson(this, type)
}

fun <T> T.toJson(): String {
    return Gson().toJson(this) ?: ""
}

