package com.katyrin.thundergram.view.chat.adapter

import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.ItemMyVoiceMessageBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.setChatIconFromUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyVoiceMessageHolder @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val simpleExoPlayer: SimpleExoPlayer,
    private val itemBinding: ItemMyVoiceMessageBinding
) : BaseVoiceViewHolder(simpleExoPlayer, itemBinding.root) {

    override fun bind(chatMessage: ChatMessage, position: Int): Unit =
        with(chatMessage as ChatMessage.Voice) {
            val mediaItem = MediaItem.fromUri(Uri.parse(voiceFilePath))
            itemBinding.soundSeekBar.progress = ZERO_PROGRESS
            itemBinding.speedTextView.text =
                itemBinding.root.context.getString(R.string.first_speed)
            itemBinding.userImageView.setChatIconFromUri(userPhotoPath)
            itemBinding.playImageView.setOnClickListener {
                clickPlay(position, mediaItem, itemBinding.speedTextView)
            }
            itemBinding.speedTextView.setOnClickListener {
                setRate(itemBinding.speedTextView, simpleExoPlayer::setPlaybackSpeed)
            }
            onSeekBarChange(itemBinding.soundSeekBar) {
                onStartVoice(position, mediaItem, itemBinding.speedTextView)
            }
        }

    override fun subscribeAudioProgress(position: Int) {
        coroutineScope.coroutineContext.cancelChildren()
        coroutineScope.launch {
            audioProgress().collect { progress ->
                currentVoiceDuration = simpleExoPlayer.duration
                itemBinding.soundSeekBar.progress =
                    if (position == currentPosition) progress else START_SEEK_BAR_POSITION
            }
        }
    }

    override fun setPlayIcon(icon: Int): Unit = itemBinding.playImageView.setImageResource(icon)

    override fun getProgressSeekBar(): Int = itemBinding.soundSeekBar.progress
}