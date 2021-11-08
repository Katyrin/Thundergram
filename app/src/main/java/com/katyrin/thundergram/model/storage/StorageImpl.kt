package com.katyrin.thundergram.model.storage

import android.content.Context
import android.content.SharedPreferences
import com.katyrin.thundergram.R
import javax.inject.Inject

class StorageImpl @Inject constructor(context: Context) : Storage {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

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

    override fun getMyUserId(): Long = prefs.getLong(MY_USER_ID, DEFAULT_ID)

    override fun setMyUserId(userId: Long) {
        prefs.edit()
            .apply { putLong(MY_USER_ID, userId) }
            .apply()
    }

    private companion object {
        const val SUBSCRIBE_CHAT_ID = "SUBSCRIBE_CHAT_ID"
        const val SUBSCRIBE_USER_ID = "SUBSCRIBE_USER_ID"
        const val MY_USER_ID = "MY_USER_ID"
        const val DEFAULT_ID = 0L
    }
}