package com.katyrin.thundergram.view.inputvalidators

import android.text.Editable
import android.text.TextWatcher
import java.util.regex.Pattern

class LoginCodeValidator : TextWatcher {

    internal var isValid = false

    override fun afterTextChanged(editableText: Editable) {
        isValid = isValidCode(editableText)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit

    companion object {
        private val CODE_PATTERN = Pattern.compile("[0-9]{5}")

        fun isValidCode(code: CharSequence?): Boolean =
            code != null && CODE_PATTERN.matcher(code).matches()
    }
}