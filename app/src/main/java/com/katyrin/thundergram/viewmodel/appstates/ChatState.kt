package com.katyrin.thundergram.viewmodel.appstates

import com.katyrin.thundergram.model.entities.ChatMessage

sealed class ChatState {
    data class Success(val chatMessageList: List<ChatMessage>): ChatState()
    data class Error(val message: String?): ChatState()
}
