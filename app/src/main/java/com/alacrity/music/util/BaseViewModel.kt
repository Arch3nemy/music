package com.alacrity.music.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alacrity.music.BaseEvent
import com.alacrity.music.EventHandler
import com.alacrity.music.MusicException
import com.alacrity.music.view_states.BaseViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.sql.Types.NULL

@Suppress("UNCHECKED_CAST")
abstract class BaseViewModel<T : BaseEvent, V: BaseViewState>(defaultViewState: V) : ViewModel(), EventHandler<T> {

    protected val _viewState: MutableStateFlow<V> = MutableStateFlow(defaultViewState)

    /**
     * calls function in a safe way using viewModelScope, handles exceptions and supports logging
     * by default uses IO scope
     * @param block  block: suspend () -> T - is your call.
     * @return a Result<T> which has getOrNull() -> returns response, exceptionOrNull() -> returns failure if happened
     */
    protected fun <R> launch(dispatcher: CoroutineDispatcher = Dispatchers.IO, logError: String? = null, logSuccess: String? = null, block: suspend () -> R): Result<R> {
        var result = Result.failure<R>(MusicException())
        viewModelScope.launch(dispatcher) {
             result = withContext(Dispatchers.Default) {
                 try {
                     Result.success(block()).also { logSuccess.let { Timber.d(it) } }
                 } catch (e: Exception) {
                     if (e is CancellationException) throw e
                     Result.failure<R>(e).also { logError.let { Timber.d(it) } }
                 }
             }
        }
        return result
    }

    protected fun BaseViewState.logReduce() {
        Timber.d("Reduce $this")
    }

}