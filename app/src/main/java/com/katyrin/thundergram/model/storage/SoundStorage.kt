package com.katyrin.thundergram.model.storage

interface SoundStorage {
    var isPlayState: Boolean
    var chatIdPlay: Long
    var userIdPlay: Long
    var messageIdPlay: Long
    fun setSoundSpeed(speed: Float)
    fun getSoundSpeed(): Float
}