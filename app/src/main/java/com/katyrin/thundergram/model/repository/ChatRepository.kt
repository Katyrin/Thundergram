package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.extensions.ChatKtx
import com.katyrin.thundergram.model.entities.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository : ChatKtx {
    suspend fun openChat(chatId: Long)
    suspend fun getHistoryMessages(chatId: Long): List<ChatMessage>
    fun getNewMessage(chatId: Long): Flow<List<ChatMessage>>
    suspend fun sendMessage(chatId: Long, message: String)
}