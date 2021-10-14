package com.katyrin.thundergram.model.mapping

import com.katyrin.thundergram.viewmodel.appstates.AuthState
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class LoginMappingImpl @Inject constructor() : LoginMapping {

    override suspend fun mapAuthorizationStateToLoginState(
        authorizationState: TdApi.AuthorizationState
    ): AuthState? = when (authorizationState) {
        is TdApi.AuthorizationStateReady -> AuthState.LoggedIn
        is TdApi.AuthorizationStateWaitCode -> AuthState.EnterCode
        is TdApi.AuthorizationStateWaitPassword -> AuthState.EnterPassword
        is TdApi.AuthorizationStateWaitPhoneNumber -> AuthState.EnterPhone
        else -> null
    }
}