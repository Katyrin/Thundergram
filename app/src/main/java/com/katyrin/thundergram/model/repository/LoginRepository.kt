package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.extensions.ChatKtx
import com.katyrin.libtd_ktx.extensions.UserKtx
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import kotlinx.coroutines.flow.Flow

interface LoginRepository : UserKtx, ChatKtx {
    suspend fun sendPhone(phone: String)
    suspend fun sendCode(code: String)
    suspend fun sendPassword(password: String)
    suspend fun resendAuthenticationCode()
    fun getAuthFlow(): Flow<AuthState?>
}