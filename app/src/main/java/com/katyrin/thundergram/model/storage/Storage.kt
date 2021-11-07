package com.katyrin.thundergram.model.storage

interface Storage {
    fun getLogged(): Boolean
    fun setLogged(isLogged: Boolean)
    fun getSubscribeChatId(): Long
    fun setSubscribeChatId(chatId: Long)
    fun getSubscribeUserId(): Long
    fun setSubscribeUserId(userId: Long)
}