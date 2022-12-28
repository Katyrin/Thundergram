package com.katyrin.thundergram.model.storage

import android.content.Context
import android.content.SharedPreferences
import com.katyrin.thundergram.R
import javax.inject.Inject

class SoundStorageImpl @Inject constructor(context: Context) : SoundStorage {

    override var isPlayState: Boolean = false

    override var chatIdPlay: Long = 0
    override var userIdPlay: Long = 0
    override var messageIdPlay: Long = 0

    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    override fun setSoundSpeed(speed: Float): Unit =
        prefs.edit().putFloat(SOUND_SPEED, speed).apply()

    override fun getSoundSpeed(): Float = prefs.getFloat(SOUND_SPEED, FIRST_SPEED)

    private companion object {
        const val SOUND_SPEED = "SOUND_SPEED"
        const val FIRST_SPEED = 1.0f
    }
}