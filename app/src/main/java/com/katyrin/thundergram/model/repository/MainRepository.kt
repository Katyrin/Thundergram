package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.extensions.UserKtx
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.model.entities.FirebaseEventResponse
import com.katyrin.thundergram.viewmodel.appstates.UserState
import kotlinx.coroutines.flow.Flow

interface MainRepository : UserKtx {
    fun getUserState(): UserState
    fun getSubscribedPhone(): Flow<String>
    suspend fun setCoins(coins: Long)
    suspend fun getSingleFirebaseEventResponse(): FirebaseEventResponse
    suspend fun getUpdateCoinsFlow(): Flow<FirebaseEventResponse>
    fun getNewMessageFlow(): Flow<ChatMessage>
}