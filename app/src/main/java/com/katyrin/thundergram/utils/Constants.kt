package com.katyrin.thundergram.utils

const val REQUEST_CALL_CODE = 500
const val UNSUBSCRIBE_ID = 0L

var regexPhoneNumber: String =
    "[+]?(\\d{1,5})[ ]?[-]?\\(?(\\d{3})\\)?[ ]?[-]?(\\d{3})[ ]?[-]?(\\d{2})[ ]?[-]?(\\d{2})"