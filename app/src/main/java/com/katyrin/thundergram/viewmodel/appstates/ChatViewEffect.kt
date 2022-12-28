package com.katyrin.thundergram.viewmodel.appstates

sealed class ChatViewEffect {
    object OnIsSubscribed: ChatViewEffect()
    object OnIsNotSubscribed: ChatViewEffect()
}
