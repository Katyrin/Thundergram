package com.katyrin.thundergram.viewmodel.appstates

data class SoundState(
    val isPlayState: Boolean = false,
    val isShowSoundBar: Boolean = false,
    val soundName: String = "",
    val speed: Float = 1f
)