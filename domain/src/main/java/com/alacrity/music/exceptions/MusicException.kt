package com.alacrity.music.exceptions

open class MusicException(message: String = "Undefined", exception: Throwable? = null): Exception(message, exception)