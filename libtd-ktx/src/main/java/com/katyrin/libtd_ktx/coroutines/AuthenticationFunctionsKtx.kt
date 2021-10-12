package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlin.IntArray
import kotlin.String
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Session

/**
 * Suspend function, which checks the authentication token of a bot; to log in as a bot. Works only
 * when the current authorization state is authorizationStateWaitPhoneNumber. Can be used instead of
 * setAuthenticationPhoneNumber and checkAuthenticationCode to log in.
 *
 * @param token The bot token.
 */
suspend fun TelegramFlow.checkAuthenticationBotToken(token: String?): Unit =
    sendFunctionLaunch(TdApi.CheckAuthenticationBotToken(token))

/**
 * Suspend function, which checks the authentication code. Works only when the current authorization
 * state is authorizationStateWaitCode.
 *
 * @param code The verification code received via SMS, Telegram message, phone call, or flash call.
 */
suspend fun TelegramFlow.checkAuthenticationCode(code: String?): Unit =
    sendFunctionLaunch(TdApi.CheckAuthenticationCode(code))

/**
 * Suspend function, which confirms QR code authentication on another device. Returns created
 * session on success.
 *
 * @param link A link from a QR code. The link must be scanned by the in-app camera.
 *
 * @return [Session] Contains information about one session in a Telegram application used by the
 * current user. Sessions should be shown to the user in the returned order.
 */
suspend fun TelegramFlow.confirmQrCodeAuthentication(link: String?): Session =
    sendFunctionAsync(TdApi.ConfirmQrCodeAuthentication(link))

/**
 * Suspend function, which requests QR code authentication by scanning a QR code on another logged
 * in device. Works only when the current authorization state is authorizationStateWaitPhoneNumber.
 *
 * @param otherUserIds List of user identifiers of other users currently using the client.
 */
suspend fun TelegramFlow.requestQrCodeAuthentication(otherUserIds: IntArray?): Unit =
    sendFunctionLaunch(TdApi.RequestQrCodeAuthentication(otherUserIds))

/**
 * Suspend function, which re-sends an authentication code to the user. Works only when the current
 * authorization state is authorizationStateWaitCode and the nextCodeType of the result is not null.
 */
suspend fun TelegramFlow.resendAuthenticationCode(): Unit =
    sendFunctionLaunch(TdApi.ResendAuthenticationCode())