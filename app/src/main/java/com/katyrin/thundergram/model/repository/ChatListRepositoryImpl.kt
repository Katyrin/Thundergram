package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.core.TelegramException
import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.getChat
import com.katyrin.libtd_ktx.coroutines.getChats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class ChatListRepositoryImpl @Inject constructor(
    override val api: TelegramFlow
) : ChatListRepository {

    override suspend fun getChats(): Flow<List<String>> {
        val offsetOrder = Long.MAX_VALUE
        val offsetChatId: Long = 0
        val chatLimit = 150
        return flowOf(api.getChats(offsetOrder, offsetChatId, chatLimit))
            .map { user: TdApi.Chats ->
                val chatList: MutableList<String> = mutableListOf()
                user.chatIds.forEach {
                    api.getChat(it).let { chat: TdApi.Chat ->
                        chatList.add(chat.title)
                    }
                }
                chatList
            }
            .retryWhen { cause, _ -> cause is TelegramException }
    }
}