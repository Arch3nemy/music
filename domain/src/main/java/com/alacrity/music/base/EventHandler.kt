package com.alacrity.music.base

interface EventHandler<T> {
    fun obtainEvent(event: T)
}