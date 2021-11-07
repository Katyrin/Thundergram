package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.extensions.ChatKtx
import com.katyrin.libtd_ktx.extensions.UserKtx
import com.katyrin.thundergram.model.entities.ChatListItem
import com.katyrin.thundergram.viewmodel.appstates.ChatListState
import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.TdApi

interface ChatListRepository : UserKtx, ChatKtx {
    suspend fun getChats(): List<ChatListItem>
    fun updateList(): Flow<ChatListState>
    suspend fun getAuthState(): Flow<TdApi.AuthorizationState>
    suspend fun setParameters()
    suspend fun setEncryptionKey()
}