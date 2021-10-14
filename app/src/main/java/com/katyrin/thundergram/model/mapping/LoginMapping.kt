package com.katyrin.thundergram.model.mapping

import com.katyrin.thundergram.viewmodel.appstates.AuthState
import org.drinkless.td.libcore.telegram.TdApi

interface LoginMapping {
    fun mapAuthorizationStateToLoginState(
        authorizationState: TdApi.AuthorizationState
    ): AuthState?
}