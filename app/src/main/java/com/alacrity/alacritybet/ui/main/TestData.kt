package com.alacrity.alacritybet.ui.main

sealed class TestData {
    object Loading : TestData()
    data class ShowMessage(val detail: String) : TestData()
    class Error(val message: String) : TestData()
}