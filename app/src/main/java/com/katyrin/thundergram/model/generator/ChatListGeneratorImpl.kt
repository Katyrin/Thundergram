package com.katyrin.thundergram.model.generator

import com.katyrin.thundergram.model.entities.ChatListItem
import javax.inject.Inject

class ChatListGeneratorImpl @Inject constructor() : ChatListGenerator {
    override fun generateChatList(): List<ChatListItem> = listOf(
        ChatListItem(-1, "Oleg", null, false),
        ChatListItem(-2, "Timofey", null, false),
        ChatListItem(-3, "Luba", null, false),
        ChatListItem(-4, "Alena", null, false),
        ChatListItem(-5, "Roman", null, false),
    )
}