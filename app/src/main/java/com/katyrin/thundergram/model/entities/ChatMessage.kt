package com.katyrin.thundergram.model.entities

sealed class ChatMessage {
    data class Text(
        val chatId: Long = 0,
        val userId: Long = 0,
        val message: String = "",
        val userPhotoPath: String? = "",
        val isMyMessage: Boolean = false
    ) : ChatMessage()

    data class Voice(
        val chatId: Long = 0,
        val userId: Long = 0,
        val voiceFilePath: String = "",
        val userPhotoPath: String? = "",
        val isMyMessage: Boolean = false
    ) : ChatMessage()

    data class Photo(
        val chatId: Long = 0,
        val userId: Long = 0,
        val message: String = "",
        val photoFilePath: String = "",
        val userPhotoPath: String? = "",
        val isMyMessage: Boolean = false
    ) : ChatMessage()
}
