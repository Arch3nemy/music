package com.alacrity.music.exceptions

class ControllerNotAvailableException(reason: String = "Controller is unavailable", exception: Exception? = null): MusicException(reason, exception)