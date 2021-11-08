package com.katyrin.thundergram.viewmodel.appstates

sealed class UserState {
    data class Error(val message: String?) : UserState()
    object LoggedState : UserState()
    object NotLoggedState : UserState()
    data class UpdateCoinState(val currentCoin: Int) : UserState()
    data class CallState(val phoneNumber: String, val decrementCoins: Int) : UserState()
}
