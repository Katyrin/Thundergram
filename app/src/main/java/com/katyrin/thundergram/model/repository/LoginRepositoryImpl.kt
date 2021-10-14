package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.*
import com.katyrin.libtd_ktx.flows.authorizationStateFlow
import com.katyrin.thundergram.model.mapping.LoginMapping
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val parameters: TdApi.TdlibParameters,
    private val loginMapping: LoginMapping,
    private val storage: Storage,
    override val api: TelegramFlow
) : LoginRepository {

    override suspend fun sendPhone(phone: String): Unit = api.setAuthenticationPhoneNumber(phone)

    override suspend fun sendCode(code: String): Unit = api.checkAuthenticationCode(code)

    override suspend fun sendPassword(password: String): Unit =
        api.checkAuthenticationPassword(password)

    override fun getAuthFlow(): Flow<AuthState?> =
        api.authorizationStateFlow()
            .onEach(::checkRequiredParams)
            .map(loginMapping::mapAuthorizationStateToLoginState)

    override fun getLogged(): Boolean = storage.getLogged()

    override fun setLogged(isLogged: Boolean): Unit = storage.setLogged(isLogged)

    private suspend fun checkRequiredParams(state: TdApi.AuthorizationState?) {
        when (state) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> api.setTdlibParameters(parameters)
            is TdApi.AuthorizationStateWaitEncryptionKey -> api.checkDatabaseEncryptionKey()
        }
    }
}