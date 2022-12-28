package com.katyrin.thundergram.view.chat.adapter

import android.net.Uri
import android.view.View
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.katyrin.thundergram.databinding.ItemUserVoiceMessageBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.setChatIconFromUri
import com.katyrin.thundergram.utils.setTextRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserVoiceMessageHolder(
    private val coroutineScope: CoroutineScope,
    private val exoPlayer: ExoPlayer,
    private val itemBinding: ItemUserVoiceMessageBinding,
    private val onUpdateSoundSpeed: () -> Unit,
    private val onClickPlayButton: (ChatMessage.Voice, Boolean) -> Unit,
    private val onUpdateSeekTo: (Long) -> Unit,
    private val onSubscribeUser: (chatId: Long, userId: Long, view: View, name: String) -> Unit
) : BaseVoiceViewHolder(exoPlayer, itemBinding.root) {

    override fun bind(chatMessage: ChatMessage, position: Int): Unit =
        with(chatMessage as ChatMessage.Voice) {
            subscribeAudioProgress(isPlayingPosition)
            onSoundAnimation(isPlayingPosition && isPlayState, itemBinding.playImageView)
            itemBinding.userName.text = chatMessage.userName
            val soundUri: Uri = Uri.parse(voiceFilePath)
            val mediaItem = MediaItem.fromUri(soundUri)
            itemBinding.soundSeekBar.setSampleFrom(soundUri)
            itemBinding.soundSeekBar.progress = ZERO_PROGRESS
            itemBinding.speedTextView.setTextRate(speed)
            itemBinding.userImageView.setChatIconFromUri(userPhotoPath)
            itemBinding.playImageView.setOnClickListener {
                clickPlay(mediaItem, speed, isPlayingPosition) { isPlay ->
                    onClickPlayButton(this, isPlay)
                    if (isPlay) subscribeAudioProgress(isPlayingPosition)
                }
            }
            itemBinding.speedTextView.setOnClickListener { onUpdateSoundSpeed() }
            itemBinding.soundSeekBar.onSeekBarChange {
                if (isPlayingPosition) onChangeProgress { onUpdateSeekTo(it) }
                subscribeAudioProgress(isPlayingPosition)
            }
            itemBinding.root.setOnClickListener {
                onSubscribeUser(chatId, userId, itemBinding.userImageView, userName)
            }
        }

    private fun subscribeAudioProgress(isPlayingPosition: Boolean) {
        audioProgressJob?.cancel()
        audioProgressJob = coroutineScope.launch {
            audioProgress().collect { progress ->
                if (isPlayingPosition) {
                    currentVoiceDuration = exoPlayer.duration
                    itemBinding.soundSeekBar.progress = progress.toFloat()
                }
            }
        }
    }

    override fun setPlayIcon(icon: Int): Unit = itemBinding.playImageView.setImageResource(icon)

    override fun getProgressSeekBar(): Float = itemBinding.soundSeekBar.progress
}