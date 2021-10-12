package com.katyrin.libtd_ktx.core

import org.drinkless.td.libcore.telegram.TdApi

sealed class TelegramException(message: String) : Throwable(message) {
    object ClientNotAttached : TelegramException(TELEGRAM_EXCEPTION_MESSAGE)
    class Error(message: String) : TelegramException(message)
    class UnexpectedResult(result: TdApi.Object) : TelegramException(UNEXPECTED_RESULT + result)

    private companion object {
        const val UNEXPECTED_RESULT = "unexpected result: "
        const val TELEGRAM_EXCEPTION_MESSAGE = "Client is not attached. " +
                "Please call TelegramScope.attachClient() before calling a Telegram function"
    }
}