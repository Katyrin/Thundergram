package com.katyrin.thundergram.model.storage

import android.content.Context
import android.content.SharedPreferences
import com.katyrin.thundergram.R
import javax.inject.Inject

class StorageImpl @Inject constructor(context: Context) : Storage {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    override fun getSubscribeChatId(): Long = prefs.getLong(SUBSCRIBE_CHAT_ID, DEFAULT_ID)

    override fun setSubscribeChatId(chatId: Long): Unit =
        prefs.edit().putLong(SUBSCRIBE_CHAT_ID, chatId).apply()

    override fun getSubscribeUserId(): Long = prefs.getLong(SUBSCRIBE_USER_ID, DEFAULT_ID)

    override fun setSubscribeUserId(userId: Long): Unit =
        prefs.edit().putLong(SUBSCRIBE_USER_ID, userId).apply()

    override fun getMyUserId(): Long = prefs.getLong(MY_USER_ID, DEFAULT_ID)

    override fun setMyUserId(userId: Long): Unit = prefs.edit().putLong(MY_USER_ID, userId).apply()

    override fun isVolumeOn(chatId: Long): Boolean =
        prefs.getStringSet(CHATS_ON_VOLUME, setOf())?.contains(chatId.toString()) ?: false

    override fun setIsVolumeOn(chatId: Long, isOn: Boolean) {
        val chats: MutableSet<String> =
            prefs.getStringSet(CHATS_ON_VOLUME, setOf())?.toMutableSet() ?: mutableSetOf()
        if (isOn) chats.add(chatId.toString()) else chats.remove(chatId.toString())
        prefs.edit().putStringSet(CHATS_ON_VOLUME, chats).apply()
    }

    override fun getVolumeOnChats(): List<Long> {
        val chats: MutableList<Long> = mutableListOf()
        prefs.getStringSet(CHATS_ON_VOLUME, setOf())?.forEach { chats.add(it.toLong()) }
        return chats
    }

    private companion object {
        const val CHATS_ON_VOLUME = "CHATS_ON_VOLUME"
        const val SUBSCRIBE_CHAT_ID = "SUBSCRIBE_CHAT_ID"
        const val SUBSCRIBE_USER_ID = "SUBSCRIBE_USER_ID"
        const val MY_USER_ID = "MY_USER_ID"
        const val DEFAULT_ID = 0L
    }
}