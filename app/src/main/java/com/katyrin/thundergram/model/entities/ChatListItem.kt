package com.katyrin.thundergram.model.entities

data class ChatListItem(
    val chatId: Long,
    val chatName: String,
    val chatImage: String?,
    val isVolumeOn: Boolean
)
