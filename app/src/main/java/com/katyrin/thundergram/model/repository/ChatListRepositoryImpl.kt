package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.downloadFile
import com.katyrin.libtd_ktx.coroutines.getChat
import com.katyrin.libtd_ktx.coroutines.getChats
import com.katyrin.libtd_ktx.coroutines.getRemoteFile
import com.katyrin.libtd_ktx.flows.newMessageFlow
import com.katyrin.thundergram.model.entities.ChatListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class ChatListRepositoryImpl @Inject constructor(
    override val api: TelegramFlow
) : ChatListRepository {

    override suspend fun getChats(): List<ChatListItem> =
        api.getChats(OFFSET_ORDER, OFFSET_CHAT_ID, CHAT_LIMIT).let { user: TdApi.Chats ->
            val chatList: MutableList<ChatListItem> = mutableListOf()
            user.chatIds.forEach {
                api.getChat(it).let { chat: TdApi.Chat ->
                    chatList.add(ChatListItem(chat.id, chat.title, getChatPhotoPath(chat)))
                }
            }
            chatList
        }

    private suspend fun getChatPhotoPath(chat: TdApi.Chat): String? {
        var photoPath = chat.photo?.small?.local?.path
        if (photoPath == "") {
            val file = api.getRemoteFile(chat.photo?.small?.remote?.id, null)
            val downloadedFile =
                api.downloadFile(file.id, DOWNLOAD_PRIORITY, OFFSET_FILE, FILE_SIZE_LIMIT, true)
            photoPath = downloadedFile.local.path
        }
        return photoPath
    }

    override fun updateList(): Flow<List<ChatListItem>> = api.newMessageFlow().map { getChats() }

    private companion object {
        const val OFFSET_ORDER = Long.MAX_VALUE
        const val OFFSET_CHAT_ID = 0L
        const val CHAT_LIMIT = 150
        const val DOWNLOAD_PRIORITY = 10
        const val OFFSET_FILE = 0
        const val FILE_SIZE_LIMIT = 1024
    }
}