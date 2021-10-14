package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.extensions.ChatKtx
import com.katyrin.libtd_ktx.extensions.UserKtx
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

interface ChatListRepository : UserKtx, ChatKtx {
    suspend fun getChats(): Flow<List<String>>
}