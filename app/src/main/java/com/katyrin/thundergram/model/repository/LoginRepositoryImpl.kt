package com.katyrin.thundergram.model.repository

import android.content.Context
import android.widget.Toast
import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.*
import com.katyrin.libtd_ktx.flows.authorizationStateFlow
import com.katyrin.thundergram.model.mapping.LoginMapping
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val parameters: TdApi.TdlibParameters,
    private val loginMapping: LoginMapping,
    private val storage: Storage,
    private val dispatcher: CoroutineDispatcher,
    private val context: Context,
    override val api: TelegramFlow
) : LoginRepository {

    override suspend fun sendPhone(phone: String): Unit = api.setAuthenticationPhoneNumber(phone)

    override suspend fun sendCode(code: String): Unit = api.checkAuthenticationCode(code)

    override suspend fun sendPassword(password: String): Unit =
        api.checkAuthenticationPassword(password)

    override suspend fun resendAuthenticationCode(): Unit =
        api.resendAuthenticationCode()

    override fun getAuthFlow(): Flow<AuthState?> =
        api.authorizationStateFlow()
            .onEach(::checkRequiredParams)
            .map(loginMapping::mapAuthorizationStateToLoginState)
            .flowOn(dispatcher)

    private suspend fun checkRequiredParams(state: TdApi.AuthorizationState?) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, state.toString(), Toast.LENGTH_SHORT).show()
        }
        when (state) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> api.setTdlibParameters(parameters)
            is TdApi.AuthorizationStateWaitEncryptionKey -> api.checkDatabaseEncryptionKey()
            is TdApi.AuthorizationStateReady -> storage.setMyUserId(api.getMe().id.toLong())
        }
    }
}