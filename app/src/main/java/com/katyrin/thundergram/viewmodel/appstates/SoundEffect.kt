package com.katyrin.thundergram.viewmodel.appstates

sealed class SoundEffect {
    object OnSoundButtonPlay: SoundEffect()
    object OnSoundButtonPause: SoundEffect()
}
