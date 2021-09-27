package com.alacrity.alacritybet.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(): ViewModel() {

    val testFlow get() = _testFlow
    private val _testFlow = MutableStateFlow<TestData>(TestData.Loading)



}