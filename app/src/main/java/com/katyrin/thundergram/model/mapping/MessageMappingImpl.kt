package com.katyrin.thundergram.model.mapping

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.downloadFile
import com.katyrin.libtd_ktx.coroutines.getChat
import com.katyrin.libtd_ktx.coroutines.getRemoteFile
import com.katyrin.libtd_ktx.coroutines.getUser
import com.katyrin.libtd_ktx.extensions.UserKtx
import com.katyrin.thundergram.model.entities.ChatMessage
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class MessageMappingImpl @Inject constructor(
    override val api: TelegramFlow
) : MessageMapping, UserKtx {

    override suspend fun mapTdApiMessageToChatMessage(message: TdApi.Message): ChatMessage =
        when (val content: TdApi.MessageContent? = message.content) {
            is TdApi.MessageText -> ChatMessage.Text(
                message.chatId,
                message.senderUserId.toLong(),
                api.getChat(message.chatId).title,
                api.getUser(message.senderUserId).firstName + " " + api.getUser(message.senderUserId).lastName,
                content.text.text,
                getUserPhotoPath(message.senderUserId),
                message.isOutgoing
            )
            is TdApi.MessageVoiceNote -> ChatMessage.Voice(
                message.chatId,
                message.senderUserId.toLong(),
                api.getChat(message.chatId).title,
                api.getUser(message.senderUserId).firstName + " " + api.getUser(message.senderUserId).lastName,
                getVoiceMessagePath(content.voiceNote.voice),
                getUserPhotoPath(message.senderUserId),
                message.isOutgoing
            )
            is TdApi.MessagePhoto -> ChatMessage.Photo(
                message.chatId,
                message.senderUserId.toLong(),
                api.getChat(message.chatId).title,
                api.getUser(message.senderUserId).firstName + " " + api.getUser(message.senderUserId).lastName,
                content.caption.text,
                getPhotoPath(content.photo.sizes[0].photo),
                getUserPhotoPath(message.senderUserId),
                message.isOutgoing
            )
            else -> ChatMessage.Text(
                message.chatId,
                message.senderUserId.toLong(),
                api.getChat(message.chatId).title,
                api.getUser(message.senderUserId).firstName + " " + api.getUser(message.senderUserId).lastName,
                "",
                getUserPhotoPath(message.senderUserId),
                message.isOutgoing
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
    }
}