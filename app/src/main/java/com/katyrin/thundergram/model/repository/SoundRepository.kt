package com.katyrin.thundergram.model.repository

import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.viewmodel.appstates.SoundState
import kotlinx.coroutines.flow.StateFlow

interface SoundRepository {
    var isPlayState: Boolean
    val state: StateFlow<SoundState>
    suspend fun onClickPlayButton(voice: ChatMessage.Voice, isPlayState: Boolean)
    suspend fun onClickSoundExit()
    fun onUpdateSoundSpeed()
    fun onUpdateSeekTo(positionMs: Long)
}