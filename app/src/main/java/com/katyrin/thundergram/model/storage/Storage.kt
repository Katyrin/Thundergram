package com.katyrin.thundergram.model.storage

interface Storage {
    fun getSubscribeChatId(): Long
    fun setSubscribeChatId(chatId: Long)
    fun getSubscribeUserId(): Long
    fun setSubscribeUserId(userId: Long)
    fun getMyUserId(): Long
    fun setMyUserId(userId: Long)
}