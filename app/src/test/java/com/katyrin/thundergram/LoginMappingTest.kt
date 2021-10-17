package com.katyrin.thundergram

import com.katyrin.thundergram.model.mapping.LoginMapping
import com.katyrin.thundergram.model.mapping.LoginMappingImpl
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import org.drinkless.td.libcore.telegram.TdApi
import org.junit.Assert.*
import org.junit.Test

class LoginMappingTest {

    private val loginMapping: LoginMapping = LoginMappingImpl()

    @Test
    fun loginMappingImpl_AuthStateIsAuthorizationStateReady_ReturnEquals() {
        val authState: AuthState? =
            loginMapping.mapAuthorizationStateToLoginState(TdApi.AuthorizationStateReady())
        assertEquals(authState, AuthState.LoggedIn)
    }

    @Test
    fun loginMappingImpl_AuthStateIsAuthorizationStateWaitCode_ReturnNotEquals() {
        val authState: AuthState? =
            loginMapping.mapAuthorizationStateToLoginState(TdApi.AuthorizationStateWaitCode())
        assertNotEquals(authState, AuthState.LoggedIn)
    }

    @Test
    fun loginMappingImpl_AuthStateIsAuthorizationStateWaitPassword_ReturnNotEquals() {
        val authState: AuthState? =
            loginMapping.mapAuthorizationStateToLoginState(TdApi.AuthorizationStateWaitPassword())
        assertNotEquals(authState, AuthState.LoggedIn)
    }

    @Test
    fun loginMappingImpl_AuthStateIsAuthorizationStateWaitPhoneNumber_ReturnNotEquals() {
        val authState: AuthState? =
            loginMapping.mapAuthorizationStateToLoginState(TdApi.AuthorizationStateWaitPhoneNumber())
        assertNotEquals(authState, AuthState.LoggedIn)
    }

    @Test
    fun loginMappingImpl_AuthStateIsAuthorizationStateWaitTdlibParameters_ReturnNotEquals() {
        val authState: AuthState? =
            loginMapping.mapAuthorizationStateToLoginState(TdApi.AuthorizationStateWaitTdlibParameters())
        assertNotEquals(authState, AuthState.LoggedIn)
    }

    @Test
    fun loginMappingImpl_AuthStateIsNotNull_ReturnNotNull() {
        val authState: AuthState? =
            loginMapping.mapAuthorizationStateToLoginState(TdApi.AuthorizationStateReady())
        assertNotNull(authState)
    }

    @Test
    fun loginMappingImpl_AuthStateIsNull_ReturnNull() {
        val authState: AuthState? =
            loginMapping.mapAuthorizationStateToLoginState(TdApi.AuthorizationStateWaitTdlibParameters())
        assertNull(authState)
    }

    @Test
    fun loginMappingImpl_AuthStateIsAuthorizationStateReady_ReturnSame() {
        val authState: AuthState? =
            loginMapping.mapAuthorizationStateToLoginState(TdApi.AuthorizationStateReady())
        assertSame(authState, AuthState.LoggedIn)
    }
}