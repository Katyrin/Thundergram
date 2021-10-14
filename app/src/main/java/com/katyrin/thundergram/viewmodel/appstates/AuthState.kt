package com.katyrin.thundergram.viewmodel.appstates

sealed class AuthState {
    object LoggedIn : AuthState()
    object EnterPhone : AuthState()
    object EnterCode : AuthState()
    object EnterPassword : AuthState()
}
