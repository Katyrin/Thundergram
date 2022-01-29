package com.katyrin.thundergram.model.generator

import com.katyrin.thundergram.model.entities.ChatListItem

interface ChatListGenerator {
    fun generateChatList(): List<ChatListItem>
}