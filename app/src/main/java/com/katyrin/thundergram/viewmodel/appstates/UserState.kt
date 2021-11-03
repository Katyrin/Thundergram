package com.katyrin.thundergram.viewmodel.appstates

sealed class UserState {
    data class Error(val message: String?) : UserState()
    object LoggedState : UserState()
    object NotLoggedState : UserState()
}
