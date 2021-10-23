package com.katyrin.thundergram.viewmodel.appstates

sealed class LoginScreenState {
    data class Error(val message: String?) : LoginScreenState()
    object LoggedState : LoginScreenState()
    object NotLoggedState : LoginScreenState()
}
