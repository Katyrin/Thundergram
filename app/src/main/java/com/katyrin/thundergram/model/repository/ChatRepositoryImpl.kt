package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.downloadFile
import com.katyrin.libtd_ktx.coroutines.getMessages
import com.katyrin.libtd_ktx.coroutines.getRemoteFile
import com.katyrin.libtd_ktx.coroutines.getUser
import com.katyrin.libtd_ktx.flows.newMessageFlow
import com.katyrin.thundergram.model.entities.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.MessageText
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    override val api: TelegramFlow
) : ChatRepository {

    override suspend fun getMessages(chatId: Long): List<ChatMessage> {
        val chatMessageList: MutableList<ChatMessage> = mutableListOf()
        api.getMessages(chatId).messages.forEach { message: TdApi.Message ->
            val content: TdApi.MessageContent? = message.content
            if (content is MessageText)
                chatMessageList.add(
                    ChatMessage(message.senderUserId.toLong(), content.text.text)
                )
        }
        return chatMessageList
    }

    private var chatList: MutableList<ChatMessage> = mutableListOf()

    override fun getNewMessage(chatId: Long): Flow<List<ChatMessage>> =
        api.newMessageFlow()
            .filter { it.chatId == chatId }
            .map { message: TdApi.Message ->
                val chatMessage: ChatMessage
                val content: TdApi.MessageContent? = message.content
                chatMessage = if (content is MessageText)
                    ChatMessage(message.senderUserId.toLong(), content.text.text)
                else ChatMessage(message.senderUserId.toLong(), "")
                chatList.add(0, chatMessage)
                chatList
            }

    private suspend fun getUserPhotoPath(userId: Int): String? {
        val user: TdApi.User = api.getUser(userId)
        var photoPath = user.profilePhoto?.small?.local?.path
        if (photoPath == "") {
            val file = api.getRemoteFile(user.profilePhoto?.small?.remote?.id, null)
            val downloadedFile =
                api.downloadFile(
                    file.id,
                    DOWNLOAD_PRIORITY,
                    OFFSET_FILE,
                    FILE_SIZE_LIMIT, true
                )
            photoPath = downloadedFile.local.path
        }
        return photoPath
    }

    private companion object {
        const val DOWNLOAD_PRIORITY = 10
        const val OFFSET_FILE = 0
        const val FILE_SIZE_LIMIT = 1024
    }
}