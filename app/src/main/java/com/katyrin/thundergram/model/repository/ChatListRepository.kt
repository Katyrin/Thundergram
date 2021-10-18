package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.extensions.ChatKtx
import com.katyrin.libtd_ktx.extensions.UserKtx
import com.katyrin.thundergram.model.entities.ChatListItem
import kotlinx.coroutines.flow.Flow

interface ChatListRepository : UserKtx, ChatKtx {
    suspend fun getChats(): List<ChatListItem>
    fun updateList(): Flow<List<ChatListItem>>
}