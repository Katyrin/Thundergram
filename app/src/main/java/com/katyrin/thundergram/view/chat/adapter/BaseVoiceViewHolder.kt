package com.katyrin.thundergram.view.chat.adapter

import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.katyrin.thundergram.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseVoiceViewHolder(
    private val exoPlayer: ExoPlayer,
    itemView: View
) : BaseViewHolder(itemView) {

    protected var currentPosition = START_CURRENT_POSITION
    protected var currentVoiceDuration = ZERO_VOICE_DURATION

    protected fun setRate(textView: TextView, onChangeRate: (Float) -> Unit): Unit =
        with(textView.context) {
            textView.text = when (textView.text) {
                getString(R.string.first_speed) -> {
                    onChangeRate(SECOND_SPEED)
                    getString(R.string.second_speed)
                }
                getString(R.string.second_speed) -> {
                    onChangeRate(THIRD_SPEED)
                    getString(R.string.third_speed)
                }
                else -> {
                    onChangeRate(FIRST_SPEED)
                    getString(R.string.first_speed)
                }
            }
        }

    private fun getRate(textView: TextView, onRate: (Float) -> Unit): Unit =
        with(textView.context) {
            when (textView.text) {
                getString(R.string.first_speed) -> onRate(FIRST_SPEED)
                getString(R.string.second_speed) -> onRate(SECOND_SPEED)
                getString(R.string.third_speed) -> onRate(THIRD_SPEED)
            }
        }

    protected fun audioProgress(): Flow<Int> = flow {
        var isPlaying = true
        var emitsCount = ZERO_COUNT
        while (isPlaying) {
            emit(getEmitResult())
            delay(DELAY_BETWEEN_EMITS)
            if (emitsCount != CHECK_PLAYER_COUNT) emitsCount++
            else isPlaying = exoPlayer.isPlaying
        }
        setPlayIcon(R.drawable.ic_play)
    }

    private fun getEmitResult(): Int =
        exoPlayer.currentPosition.toFloat()
            .div(exoPlayer.duration)
            .times(SEEK_BAR_FULL_SIZE).toInt()

    private fun getMsPosition(progress: Int): Long =
        currentVoiceDuration.toFloat().div(SEEK_BAR_FULL_SIZE).times(progress).toLong()

    protected fun onSeekBarChange(seekBar: SeekBar, onRestart: () -> Unit) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                onRestart()
            }
        })
    }

    protected fun clickPlay(position: Int, mediaItem: MediaItem, speedTextView: TextView): Unit =
        if (exoPlayer.isPlaying && position == currentPosition) onStopVoice()
        else onStartVoice(position, mediaItem, speedTextView)

    private fun onStopVoice() {
        setPlayIcon(R.drawable.ic_play)
        exoPlayer.pause()
    }

    protected fun onStartVoice(position: Int, mediaItem: MediaItem, speedTextView: TextView) {
        var progress = getProgressSeekBar()
        if (progress > PROGRESS_RESTART_SIZE) progress = ZERO_PROGRESS
        currentPosition = position
        setPlayIcon(R.drawable.ic_pause)
        getRate(speedTextView, exoPlayer::setPlaybackSpeed)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.seekTo(getMsPosition(progress))
        exoPlayer.play()
        subscribeAudioProgress(position)
    }

    protected abstract fun setPlayIcon(icon: Int)
    protected abstract fun getProgressSeekBar(): Int
    protected abstract fun subscribeAudioProgress(position: Int)

    protected companion object {
        const val FIRST_SPEED = 1.0f
        const val SECOND_SPEED = 2.0f
        const val THIRD_SPEED = 2.5f
        const val ZERO_COUNT = 0
        const val ZERO_PROGRESS = 0
        const val SEEK_BAR_FULL_SIZE = 100
        const val PROGRESS_RESTART_SIZE = 95
        const val DELAY_BETWEEN_EMITS = 10L
        const val CHECK_PLAYER_COUNT = 50
        const val START_CURRENT_POSITION = -1
        const val START_SEEK_BAR_POSITION = 1
        const val ZERO_VOICE_DURATION = 0L
    }
}