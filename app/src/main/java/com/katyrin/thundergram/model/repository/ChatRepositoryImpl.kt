package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.getChatHistory
import com.katyrin.libtd_ktx.coroutines.openChat
import com.katyrin.libtd_ktx.coroutines.sendMessage
import com.katyrin.libtd_ktx.flows.newMessageFlow
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.model.mapping.MessageMapping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val messageMapping: MessageMapping,
    override val api: TelegramFlow
) : ChatRepository {

    private val voiceSpeedFlow: StateFlow<Unit>? = null
    private val _voiceSpeedFlow: MutableSharedFlow<Unit> = MutableSharedFlow()

    override suspend fun openChat(chatId: Long): Unit =
        withContext(Dispatchers.IO) { api.openChat(chatId) }

    override suspend fun getHistoryMessages(chatId: Long): List<ChatMessage> =
        withContext(Dispatchers.IO) {
            val chatMessageList: MutableList<ChatMessage> = mutableListOf()
            val messages: Array<TdApi.Message> = getArrayMessage(chatId)
            messages.forEach { message: TdApi.Message ->
                chatMessageList.add(messageMapping.mapTdApiMessageToChatMessage(message))
            }
            chatMessageList
        }

    private suspend fun getArrayMessage(chatId: Long): Array<TdApi.Message> =
        api.getChatHistory(chatId, FROM_MESSAGE_ID, OFFSET_MESSAGE, MESSAGE_LIMIT, false).messages

    override fun getNewMessage(chatId: Long): Flow<List<ChatMessage>> = flowOf(
        api.newMessageFlow().filter { it.chatId == chatId },
        voiceSpeedFlow
    ).map { getHistoryMessages(chatId) }

    override suspend fun sendMessage(chatId: Long, message: String): Unit =
        withContext(Dispatchers.IO) {
            api.sendMessage(
                chatId = chatId,
                inputMessageContent =
                TdApi.InputMessageText(TdApi.FormattedText(message, arrayOf()), true, true)
            )
        }

    override fun emitNewMessage() {
        _voiceSpeedFlow.tryEmit(Unit)
    }

    private companion object {
        const val FROM_MESSAGE_ID = 0L
        const val OFFSET_MESSAGE = 0
        const val MESSAGE_LIMIT = 100
    }
}