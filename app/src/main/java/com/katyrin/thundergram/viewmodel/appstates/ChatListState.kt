package com.katyrin.thundergram.viewmodel.appstates

import com.katyrin.thundergram.model.entities.ChatListItem

sealed class ChatListState {
    data class Success(
        val chatList: List<ChatListItem>,
        val isStartService: Boolean = false
    ) : ChatListState()

    data class Error(val message: String?) : ChatListState()
}
