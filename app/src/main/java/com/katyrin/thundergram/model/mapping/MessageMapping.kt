package com.katyrin.thundergram.model.mapping

import com.katyrin.thundergram.model.entities.ChatMessage
import org.drinkless.td.libcore.telegram.TdApi

interface MessageMapping {
    suspend fun mapTdApiMessageToChatMessage(message: TdApi.Message): ChatMessage
}