package com.katyrin.thundergram.model.repository

import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.model.storage.SoundStorage
import com.katyrin.thundergram.utils.setNewSpeed
import com.katyrin.thundergram.utils.update
import com.katyrin.thundergram.viewmodel.appstates.SoundState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SoundRepositoryImpl @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val chatRepository: ChatRepository,
    private val soundStorage: SoundStorage
) : SoundRepository {

    private val soundCoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var soundPlayingJob: Job? = null

    private var isPlaying: Boolean = false
    private var isUpdateSeek: Boolean = false
    private var seekToMs: Long = START_SEEK_POSITION

    override var isPlayState: Boolean = false
        set(value) {
            field = value
            soundStorage.isPlayState = value
            if (value) {
                exoPlayer.play()
                checkSoundPlaying()
            } else exoPlayer.pause()
        }

    private val _state: MutableStateFlow<SoundState> =
        MutableStateFlow(SoundState(speed = soundStorage.getSoundSpeed()))
    override val state: StateFlow<SoundState> = _state.asStateFlow()

    override suspend fun onClickPlayButton(voice: ChatMessage.Voice, isPlayState: Boolean) {
        if (_state.value.isPlayState == isPlayState)
            _state.update { copy(isPlayState = !isPlayState) }
        this.isPlayState = isPlayState
        soundStorage.chatIdPlay = voice.chatId
        soundStorage.userIdPlay = voice.userId
        soundStorage.messageIdPlay = voice.messageId
        _state.update {
            copy(isPlayState = isPlayState, isShowSoundBar = true, soundName = voice.userName)
        }
        chatRepository.emitNewMessage()
    }

    override suspend fun onClickSoundExit() {
        isPlayState = false
        _state.update { copy(isPlayState = false, isShowSoundBar = false) }
        exoPlayer.stop()
    }

    private fun checkSoundPlaying() {
        isPlaying = true
        isUpdateSeek = false
        soundPlayingJob?.cancel()
        soundPlayingJob = soundCoroutineScope.launch {
            while (!exoPlayer.isPlaying) delay(DELAY_BETWEEN_EMITS)
            while (isPlaying) {
                delay(DELAY_BETWEEN_EMITS)
                checkNextSound()
                checkSeekUpdate()
                delay(DELAY_BETWEEN_EMITS)
            }
            onClickSoundExit()
        }
    }

    private suspend fun checkNextSound() {
        if (!exoPlayer.isPlaying && isPlayState) {
            var nextVoice: ChatMessage.Voice? = null
            chatRepository.getHistoryMessages(soundStorage.chatIdPlay)
                .filterIsInstance<ChatMessage.Voice>()
                .map { if (it.messageId > soundStorage.messageIdPlay) nextVoice = it }
            if (nextVoice != null) nextVoice?.apply {
                soundStorage.chatIdPlay = chatId
                soundStorage.userIdPlay = userId
                soundStorage.messageIdPlay = messageId
                chatRepository.emitNewMessage()
                onStartVoice(MediaItem.fromUri(Uri.parse(voiceFilePath)))
                _state.update { copy(isPlayState = isPlayState, soundName = userName) }
                while (!exoPlayer.isPlaying) delay(DELAY_BETWEEN_EMITS)
            } else isPlaying = exoPlayer.isPlaying
        }
    }

    private suspend fun checkSeekUpdate() {
        if (isUpdateSeek) {
            exoPlayer.seekTo(seekToMs)
            isUpdateSeek = false
            while (!exoPlayer.isPlaying) delay(DELAY_BETWEEN_EMITS)
        }
    }

    private fun onStartVoice(mediaItem: MediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.seekTo(START_SEEK_POSITION)
        exoPlayer.play()
    }

    override fun onUpdateSoundSpeed() {
        val newSpeed = soundStorage.getSoundSpeed().setNewSpeed()
        soundStorage.setSoundSpeed(newSpeed)
        exoPlayer.setPlaybackSpeed(newSpeed)
        chatRepository.emitNewMessage()
        _state.update { copy(speed = newSpeed) }
    }

    override fun onUpdateSeekTo(positionMs: Long) {
        seekToMs = positionMs
        isUpdateSeek = true
    }

    private companion object {
        const val DELAY_BETWEEN_EMITS = 10L
        const val START_SEEK_POSITION = 0L
    }
}