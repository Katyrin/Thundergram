package com.katyrin.thundergram.model.entities

sealed class ChatMessage {
    data class Text(
        val userId: Long,
        val message: String,
        val userPhotoPath: String?,
        val isMyMessage: Boolean
    ) : ChatMessage()

    data class Voice(
        val userId: Long,
        val voiceFilePath: String,
        val userPhotoPath: String?,
        val isMyMessage: Boolean
    ) : ChatMessage()

    data class Photo(
        val userId: Long,
        val message: String,
        val photoFilePath: String,
        val userPhotoPath: String?,
        val isMyMessage: Boolean
    ) : ChatMessage()
}
