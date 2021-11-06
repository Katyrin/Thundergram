package com.katyrin.thundergram.view.chat.adapter

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_EXCLUSIVE_INCLUSIVE
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

abstract class BaseTextVewHolder(
    itemView: View,
    private val onPhoneNumberClick: (String) -> Unit
) : BaseViewHolder(itemView) {

    protected fun setSpannableString(textView: TextView) {
        val message: CharSequence = textView.text
        if (message is Spannable) with(message) {
            val urls: Array<URLSpan> = getSpans(START_SPAN, length, URLSpan::class.java)
            textView.text = getSpannableString(message, urls)
        }
    }

    private fun getSpannableString(
        message: Spannable,
        urls: Array<URLSpan>
    ): SpannableStringBuilder = SpannableStringBuilder(message).also { style ->
        style.clearSpans()
        for (urlSpan in urls)
            style.setSpan(
                getPhoneSpan(urlSpan.url),
                message.getSpanStart(urlSpan),
                message.getSpanEnd(urlSpan),
                SPAN_EXCLUSIVE_INCLUSIVE
            )
    }

    private fun getPhoneSpan(phoneNumber: String) = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onPhoneNumberClick(phoneNumber)
        }
    }

    private companion object {
        const val START_SPAN = 0
    }
}