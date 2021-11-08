package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.extensions.UserKtx
import com.katyrin.thundergram.model.entities.FirebaseEventResponse
import com.katyrin.thundergram.viewmodel.appstates.UserState
import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.TdApi

interface MainRepository : UserKtx {
    suspend fun getUserState(): UserState
    fun getSubscribedPhone(): Flow<String>
    suspend fun setCoins(coins: Long)
    suspend fun getSingleFirebaseEventResponse(): FirebaseEventResponse
    suspend fun getUpdateCoinsFlow(): Flow<FirebaseEventResponse>
}