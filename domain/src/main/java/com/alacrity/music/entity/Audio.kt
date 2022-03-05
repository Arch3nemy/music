package com.alacrity.music.entity


data class Audio(
    val id: String,
    val title: String,
    val path: String,
    val artist: String,
    val displayName: String,
    val duration: Long,
    val albumId: Long,
    var albumArt: Any?
)