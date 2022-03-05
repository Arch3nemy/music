package com.alacrity.music.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alacrity.music.base.BaseEvent
import com.alacrity.music.base.EventHandler
import com.alacrity.music.utils.DEFAULT_PREFIX
import com.alacrity.music.utils.logThis
import com.alacrity.music.view_states.BaseViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

@Suppress("UNCHECKED_CAST")
abstract class BaseViewModel<T : BaseEvent, V : BaseViewState>(defaultViewState: V) : ViewModel(),
    EventHandler<T> {

    protected val baseModelViewState: MutableStateFlow<V> = MutableStateFlow(defaultViewState)

    /**
     * calls function in a safe way using viewModelScope, handles exceptions and supports logging
     * by default uses IO scope
     * @param block  block: suspend () -> T - is your call.
     * @return a Result<T> which has getOrNull() -> returns response, exceptionOrNull() -> returns failure if happened
     */
/*    protected fun <R> launch(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        logError: String? = null,
        logSuccess: String? = null,
        onSuccess: ((R) -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null,
        block: suspend () -> R,
    ) {
        viewModelScope.launch(dispatcher) {
            withContext(Dispatchers.Default) {
                try {
                    Result.success(block()).also { logSuccess.let { it.logThis() } }
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    Result.failure<R>(e).also { logError.let { it.logThis() } }
                }
            }.fold(
                onSuccess = { runOnMainDispatcher { onSuccess?.invoke(it) } },
                onFailure = { runOnMainDispatcher { onFailure?.invoke(it) } }
            )
        }
    }*/

    /**
     * Launches function is viewModelScope
     * use with safeCall
     * @param block is suspend function(safeCall most of the time)
     */
    protected fun launch(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
        }
    }

    protected suspend fun <T> safeCall(
        successLogs: String? = null,
        errorLogs: String? = null,
        block: suspend () -> T
    ): Result<T> {
        val x = coroutineScope {
            async {
                try {
                    val result = block.invoke()
                    successLogs?.let { it.logThis() }
                    Result.success(result)
                } catch (e: Exception) {
                    if (e is CancellationException) {
                        throw e
                    }
                    errorLogs?.let { it.logThis() }
                    Result.failure(e)
                }
            }
        }
        return x.await()
    }

    protected suspend fun <T> Result<T>.foldOnMainDispatcher(
        onSuccess: (T) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            fold(onSuccess, onFailure)
        }
    }

    protected suspend fun runOnMainDispatcher(block: () -> Unit) {
        withContext(Dispatchers.Main) {
            block()
        }
    }

    protected fun BaseViewState.logReduce(event: BaseEvent) {
        val viewStateLog = if (toString().length < 10) toString() else toString().substring(0, 10)
        Timber.d("$DEFAULT_PREFIX Reduce: event $event in state $viewStateLog}")
    }

}