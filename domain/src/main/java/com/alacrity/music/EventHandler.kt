package com.alacrity.music

interface EventHandler<T> {
    fun obtainEvent(event: T)
}