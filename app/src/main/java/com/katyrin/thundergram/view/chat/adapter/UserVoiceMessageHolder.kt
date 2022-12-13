package com.katyrin.thundergram.view.chat.adapter

import android.net.Uri
import android.view.View
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.ItemUserVoiceMessageBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.setChatIconFromUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class UserVoiceMessageHolder(
    private val coroutineScope: CoroutineScope,
    private val exoPlayer: ExoPlayer,
    private val itemBinding: ItemUserVoiceMessageBinding,
    private val onSubscribeUser: (chatId: Long, userId: Long, view: View, name: String) -> Unit
) : BaseVoiceViewHolder(exoPlayer, itemBinding.root) {

    override fun bind(chatMessage: ChatMessage, position: Int): Unit =
        with(chatMessage as ChatMessage.Voice) {
            itemBinding.userName.text = chatMessage.userName
            val mediaItem = MediaItem.fromUri(Uri.parse(voiceFilePath))
            itemBinding.soundSeekBar.progress = ZERO_PROGRESS
            itemBinding.speedTextView.text =
                itemBinding.root.context.getString(R.string.first_speed)
            itemBinding.userImageView.setChatIconFromUri(userPhotoPath)
            itemBinding.playImageView.setOnClickListener {
                clickPlay(position, mediaItem, itemBinding.speedTextView)
            }
            itemBinding.speedTextView.setOnClickListener {
                setRate(itemBinding.speedTextView, exoPlayer::setPlaybackSpeed)
            }
            onSeekBarChange(itemBinding.soundSeekBar) {
                onStartVoice(position, mediaItem, itemBinding.speedTextView)
            }
            itemBinding.root.setOnClickListener {
                onSubscribeUser(chatId, userId, itemBinding.userImageView, userName)
            }
        }

    override fun subscribeAudioProgress(position: Int) {
        coroutineScope.coroutineContext.cancelChildren()
        coroutineScope.launch {
            audioProgress().collect { progress ->
                currentVoiceDuration = exoPlayer.duration
                itemBinding.soundSeekBar.progress =
                    if (position == currentPosition) progress else START_SEEK_BAR_POSITION
            }
        }
    }

    override fun setPlayIcon(icon: Int): Unit = itemBinding.playImageView.setImageResource(icon)

    override fun getProgressSeekBar(): Int = itemBinding.soundSeekBar.progress
}