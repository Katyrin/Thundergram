package com.katyrin.thundergram.view.inputvalidators

import android.text.Editable
import android.text.TextWatcher
import java.util.regex.Pattern

class PhoneNumberValidator : TextWatcher {

    internal var isValid = false

    override fun afterTextChanged(editableText: Editable) {
        isValid = isValidPhoneNumber(editableText)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit

    companion object {
        private val PHONE_PATTERN = Pattern.compile("[+][0-9]{7,}")

        fun isValidPhoneNumber(phoneNumber: CharSequence?): Boolean =
            phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches()
    }
}