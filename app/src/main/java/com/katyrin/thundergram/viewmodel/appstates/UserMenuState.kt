package com.katyrin.thundergram.viewmodel.appstates

sealed class UserMenuState {
    data class ShowSubscribe(val chatId: Long, val userId: Long) : UserMenuState()
    object ShowUnsubscribe : UserMenuState()
}
