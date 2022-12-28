package com.katyrin.thundergram.view.chat.adapter

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.katyrin.thundergram.R
import com.katyrin.thundergram.view.waveview.SeekBarOnProgressChanged
import com.katyrin.thundergram.view.waveview.WaveformSeekBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseVoiceViewHolder(
    private val exoPlayer: ExoPlayer,
    itemView: View
) : BaseViewHolder(itemView) {

    protected var currentVoiceDuration = ZERO_VOICE_DURATION
    protected var audioProgressJob: Job? = null

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

    private fun getMsPosition(progress: Float): Long =
        currentVoiceDuration.toFloat().div(SEEK_BAR_FULL_SIZE).times(progress).toLong()

    protected fun WaveformSeekBar.onSeekBarChange(onRestart: () -> Unit) {
        onProgressChanged = object : SeekBarOnProgressChanged {
            override fun onProgressChanged(progress: Float): Unit = onRestart()
        }
    }

    protected fun clickPlay(
        mediaItem: MediaItem,
        speed: Float,
        isPlayingPosition: Boolean,
        onCLickPlayButton: (Boolean) -> Unit
    ): Unit =
        if (exoPlayer.isPlaying && isPlayingPosition) onPauseVoice(onCLickPlayButton)
        else onStartVoice(mediaItem, speed, onCLickPlayButton)

    private fun onPauseVoice(onCLickPlayButton: (Boolean) -> Unit) {
        setPlayIcon(R.drawable.ic_play)
        onCLickPlayButton(false)
    }

    private fun onStartVoice(
        mediaItem: MediaItem,
        speed: Float,
        onCLickPlayButton: (Boolean) -> Unit
    ) {
        setPlayIcon(R.drawable.ic_pause)
        exoPlayer.setPlaybackSpeed(speed)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        onChangeProgress(exoPlayer::seekTo)
        onCLickPlayButton(true)
    }

    protected fun onSoundAnimation(isPlayState: Boolean, imageView: ImageView) {
        val drawableResource: Int = if (isPlayState) R.drawable.ic_pause else R.drawable.ic_play
        val appCompatDrawable = AppCompatResources.getDrawable(imageView.context, drawableResource)
        imageView.setImageDrawable(appCompatDrawable)
        val drawable: Drawable = imageView.drawable
        if (drawable is AnimatedVectorDrawable) drawable.start()
    }

    protected fun onChangeProgress(onUpdateSeekTo: (Long) -> Unit) {
        var progress = getProgressSeekBar()
        if (progress > PROGRESS_RESTART_SIZE) progress = ZERO_PROGRESS
        onUpdateSeekTo(getMsPosition(progress))
    }

    protected abstract fun setPlayIcon(icon: Int)
    protected abstract fun getProgressSeekBar(): Float

    protected companion object {
        const val ZERO_COUNT = 0
        const val ZERO_PROGRESS = 0f
        const val SEEK_BAR_FULL_SIZE = 100
        const val PROGRESS_RESTART_SIZE = 98
        const val DELAY_BETWEEN_EMITS = 10L
        const val CHECK_PLAYER_COUNT = 50
        const val ZERO_VOICE_DURATION = 0L
    }
}