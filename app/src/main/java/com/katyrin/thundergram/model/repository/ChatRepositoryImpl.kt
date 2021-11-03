package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.*
import com.katyrin.libtd_ktx.flows.newMessageFlow
import com.katyrin.thundergram.model.entities.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.MessageText
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    override val api: TelegramFlow
) : ChatRepository {

    override suspend fun getHistoryMessages(chatId: Long): List<ChatMessage> =
        withContext(Dispatchers.IO) {
            val chatMessageList: MutableList<ChatMessage> = mutableListOf()
            val messages: Array<TdApi.Message> = getArrayMessage(chatId)
            messages.forEach { message: TdApi.Message ->
                when (val content: TdApi.MessageContent? = message.content) {
                    is MessageText -> chatMessageList.add(
                        ChatMessage.Text(
                            message.senderUserId.toLong(),
                            content.text.text,
                            getUserPhotoPath(message.senderUserId),
                            message.isOutgoing
                        )
                    )
                    is TdApi.MessageVoiceNote -> chatMessageList.add(
                        ChatMessage.Voice(
                            message.senderUserId.toLong(),
                            getVoiceMessagePath(content.voiceNote.voice),
                            getUserPhotoPath(message.senderUserId),
                            message.isOutgoing
                        )
                    )
                    is TdApi.MessagePhoto -> chatMessageList.add(
                        ChatMessage.Photo(
                            message.senderUserId.toLong(),
                            content.caption.text,
                            getPhotoPath(content.photo.sizes[0].photo),
                            getUserPhotoPath(message.senderUserId),
                            message.isOutgoing
                        )
                    )
                }
            }
            chatMessageList
        }

    private suspend fun getArrayMessage(chatId: Long): Array<TdApi.Message> =
        api.getChatHistory(chatId, FROM_MESSAGE_ID, OFFSET_MESSAGE, MESSAGE_LIMIT, false)
            .messages

    override fun getNewMessage(chatId: Long): Flow<List<ChatMessage>> =
        api.newMessageFlow()
            .filter { it.chatId == chatId }
            .map { getHistoryMessages(chatId) }

    override suspend fun sendMessage(chatId: Long, message: String): Unit =
        withContext(Dispatchers.IO) {
            api.sendMessage(
                chatId = chatId,
                inputMessageContent =
                TdApi.InputMessageText(TdApi.FormattedText(message, arrayOf()), true, true)
            )
        }

    private suspend fun getUserPhotoPath(userId: Int): String? {
        val user: TdApi.User = api.getUser(userId)
        var photoPath = user.profilePhoto?.small?.local?.path
        if (photoPath == "") photoPath = downloadFile(user.profilePhoto?.small?.remote?.id)
        return photoPath
    }

    private suspend fun getVoiceMessagePath(voiceFile: TdApi.File): String {
        var voicePath = voiceFile.local.path
        if (voicePath == "") voicePath = downloadFile(voiceFile.remote.id)
        val size = voiceFile.local.downloadedSize
        val siz2 = voiceFile.remote.uploadedSize
        siz2.toString()
        size.toString()
        return voicePath
    }

    private suspend fun getPhotoPath(photoFile: TdApi.File): String {
        var photoPath = photoFile.local.path
        if (photoPath == "") photoPath = downloadFile(photoFile.remote.id)
        return photoPath
    }

    private suspend fun downloadFile(fileId: String?): String =
        api.downloadFile(
            api.getRemoteFile(fileId, null).id,
            DOWNLOAD_PRIORITY,
            OFFSET_FILE,
            FILE_SIZE_LIMIT, false
        ).local.path

    private companion object {
        const val DOWNLOAD_PRIORITY = 16
        const val OFFSET_FILE = 0
        const val FILE_SIZE_LIMIT = 0
        const val FROM_MESSAGE_ID = 0L
        const val OFFSET_MESSAGE = 0
        const val MESSAGE_LIMIT = 1000
    }
}