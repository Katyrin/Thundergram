package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthenticationCodeInfo
import org.drinkless.td.libcore.telegram.TdApi.PhoneNumberAuthenticationSettings

/**
 * Suspend function, which changes the phone number of the user and sends an authentication code to
 * the user's new phone number. On success, returns information about the sent code.
 *
 * @param phoneNumber The new phone number of the user in international format.
 * @param settings Settings for the authentication of the user's phone number.
 *
 * @return [AuthenticationCodeInfo] Information about the authentication code that was sent.
 */
suspend fun TelegramFlow.changePhoneNumber(
    phoneNumber: String?,
    settings: PhoneNumberAuthenticationSettings? = null
): AuthenticationCodeInfo = sendFunctionAsync(TdApi.ChangePhoneNumber(phoneNumber, settings))

/**
 * Suspend function, which checks the authentication code sent to confirm a new phone number of the
 * user.
 *
 * @param code Verification code received by SMS, phone call or flash call.
 */
suspend fun TelegramFlow.checkChangePhoneNumberCode(code: String?): Unit =
    sendFunctionLaunch(TdApi.CheckChangePhoneNumberCode(code))

/**
 * Suspend function, which checks phone number confirmation code.
 *
 * @param code The phone number confirmation code.
 */
suspend fun TelegramFlow.checkPhoneNumberConfirmationCode(code: String?): Unit =
    sendFunctionLaunch(TdApi.CheckPhoneNumberConfirmationCode(code))

/**
 * Suspend function, which checks the phone number verification code for Telegram Passport.
 *
 * @param code Verification code.
 */
suspend fun TelegramFlow.checkPhoneNumberVerificationCode(code: String?): Unit =
    sendFunctionLaunch(TdApi.CheckPhoneNumberVerificationCode(code))

/**
 * Suspend function, which re-sends the authentication code sent to confirm a new phone number for
 * the user. Works only if the previously received authenticationCodeInfo nextCodeType was not null.
 *
 * @return [AuthenticationCodeInfo] Information about the authentication code that was sent.
 */
suspend fun TelegramFlow.resendChangePhoneNumberCode(): AuthenticationCodeInfo =
    sendFunctionAsync(TdApi.ResendChangePhoneNumberCode())

/**
 * Suspend function, which resends phone number confirmation code.
 *
 * @return [AuthenticationCodeInfo] Information about the authentication code that was sent.
 */
suspend fun TelegramFlow.resendPhoneNumberConfirmationCode(): AuthenticationCodeInfo =
    sendFunctionAsync(TdApi.ResendPhoneNumberConfirmationCode())

/**
 * Suspend function, which re-sends the code to verify a phone number to be added to a user's
 * Telegram Passport.
 *
 * @return [AuthenticationCodeInfo] Information about the authentication code that was sent.
 */
suspend fun TelegramFlow.resendPhoneNumberVerificationCode(): AuthenticationCodeInfo =
    sendFunctionAsync(TdApi.ResendPhoneNumberVerificationCode())

/**
 * Suspend function, which sends phone number confirmation code. Should be called when user presses
 * &quot;https://t.me/confirmphone?phone=*******&amp;hash=**********&quot; or
 * &quot;tg://confirmphone?phone=*******&amp;hash=**********&quot; link.
 *
 * @param hash Value of the &quot;hash&quot; parameter from the link.
 * @param phoneNumber Value of the &quot;phone&quot; parameter from the link.
 * @param settings Settings for the authentication of the user's phone number.
 *
 * @return [AuthenticationCodeInfo] Information about the authentication code that was sent.
 */
suspend fun TelegramFlow.sendPhoneNumberConfirmationCode(
    hash: String?,
    phoneNumber: String?,
    settings: PhoneNumberAuthenticationSettings?
): AuthenticationCodeInfo = sendFunctionAsync(
    TdApi.SendPhoneNumberConfirmationCode(hash, phoneNumber, settings)
)

/**
 * Suspend function, which sends a code to verify a phone number to be added to a user's Telegram
 * Passport.
 *
 * @param phoneNumber The phone number of the user, in international format.
 * @param settings Settings for the authentication of the user's phone number.
 *
 * @return [AuthenticationCodeInfo] Information about the authentication code that was sent.
 */
suspend fun TelegramFlow.sendPhoneNumberVerificationCode(
    phoneNumber: String?,
    settings: PhoneNumberAuthenticationSettings?
): AuthenticationCodeInfo =
    sendFunctionAsync(TdApi.SendPhoneNumberVerificationCode(phoneNumber, settings))

/**
 * Suspend function, which sets the phone number of the user and sends an authentication code to the
 * user. Works only when the current authorization state is authorizationStateWaitPhoneNumber, or if
 * there is no pending authentication query and the current authorization state is
 * authorizationStateWaitCode, authorizationStateWaitRegistration, or authorizationStateWaitPassword.
 *
 * @param phoneNumber The phone number of the user, in international format.
 * @param settings Settings for the authentication of the user's phone number.
 */
suspend fun TelegramFlow.setAuthenticationPhoneNumber(
    phoneNumber: String?,
    settings: PhoneNumberAuthenticationSettings? = null
): Unit = sendFunctionLaunch(TdApi.SetAuthenticationPhoneNumber(phoneNumber, settings))

/**
 * Suspend function, which shares the phone number of the current user with a mutual contact.
 * Supposed to be called when the user clicks on chatActionBarSharePhoneNumber.
 *
 * @param userId Identifier of the user with whom to share the phone number. The user must be a
 * mutual contact.
 */
suspend fun TelegramFlow.sharePhoneNumber(userId: Int): Unit =
    sendFunctionLaunch(TdApi.SharePhoneNumber(userId))