package com.katyrin.thundergram.view.main

interface CallListener {
    fun onPhoneCallNumber(phoneNumber: String, decrementCoin: Int)
}