package com.katyrin.thundergram.utils

const val REQUEST_CALL_CODE = 500
const val UNSUBSCRIBE_ID = 0L
const val CALL_50_COIN = "call_50_coin"
const val CALL_100_COIN = "call_100_coin"
const val CALL_500_COIN = "call_500_coin"
const val COIN_50 = 50
const val COIN_100 = 100
const val COIN_500 = 500

var regexPhoneNumber: String =
    "[+]?(\\d{1,5})[ ]?[-]?\\(?(\\d{3})\\)?[ ]?[-]?(\\d{3})[ ]?[-]?(\\d{2})[ ]?[-]?(\\d{2})"

const val PARAMETERS_MESSAGE_ERROR =
    "Initialization parameters are needed: call setTdlibParameters first"

const val PACKAGE = "package"