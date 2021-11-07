package com.katyrin.thundergram.model.storage

import android.content.Context
import android.content.SharedPreferences
import com.katyrin.thundergram.R
import javax.inject.Inject

class StorageImpl @Inject constructor(context: Context) : Storage {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    override fun getLogged(): Boolean = prefs.getBoolean(IS_LOGGED, false)

    override fun setLogged(isLogged: Boolean) {
        prefs.edit()
            .apply { putBoolean(IS_LOGGED, isLogged) }
            .apply()
    }

    override fun getSubscribeChatId(): Long = prefs.getLong(SUBSCRIBE_CHAT_ID, DEFAULT_ID)

    override fun setSubscribeChatId(chatId: Long) {
        prefs.edit()
            .apply { putLong(SUBSCRIBE_CHAT_ID, chatId) }
            .apply()
    }

    override fun getSubscribeUserId(): Long = prefs.getLong(SUBSCRIBE_USER_ID, DEFAULT_ID)

    override fun setSubscribeUserId(userId: Long) {
        prefs.edit()
            .apply { putLong(SUBSCRIBE_USER_ID, userId) }
            .apply()
    }

    private companion object {
        const val IS_LOGGED = "IS_LOGGED"
        const val SUBSCRIBE_CHAT_ID = "SUBSCRIBE_CHAT_ID"
        const val SUBSCRIBE_USER_ID = "SUBSCRIBE_USER_ID"
        const val DEFAULT_ID = 0L
    }
}