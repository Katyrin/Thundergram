package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which deletes a Telegram Passport element.
 *
 * @param type Element type.
 */
suspend fun TelegramFlow.deletePassportElement(type: PassportElementType?): Unit =
    sendFunctionLaunch(DeletePassportElement(type))

/**
 * Suspend function, which returns all available Telegram Passport elements.
 *
 * @param password Password of the current user.
 *
 * @return [PassportElements] Contains information about saved Telegram Passport elements.
 */
suspend fun TelegramFlow.getAllPassportElements(password: String?): PassportElements =
    sendFunctionAsync(GetAllPassportElements(password))

/**
 * Suspend function, which returns a Telegram Passport authorization form for sharing data with a
 * service.
 *
 * @param botUserId User identifier of the service's bot.
 * @param scope Telegram Passport element types requested by the service.
 * @param publicKey Service's publicKey.
 * @param nonce Authorization form nonce provided by the service.
 *
 * @return [PassportAuthorizationForm] Contains information about a Telegram Passport authorization
 * form that was requested.
 */
suspend fun TelegramFlow.getPassportAuthorizationForm(
    botUserId: Int,
    scope: String?,
    publicKey: String?,
    nonce: String?
): PassportAuthorizationForm =
    sendFunctionAsync(GetPassportAuthorizationForm(botUserId, scope, publicKey, nonce))

/**
 * Suspend function, which returns already available Telegram Passport elements suitable for
 * completing a Telegram Passport authorization form. Result can be received only once for each
 * authorization form.
 *
 * @param authorizationFormId Authorization form identifier.
 * @param password Password of the current user.
 *
 * @return [PassportElementsWithErrors] Contains information about a Telegram Passport elements and
 * corresponding errors.
 */
suspend fun TelegramFlow.getPassportAuthorizationFormAvailableElements(
    authorizationFormId: Int,
    password: String?
): PassportElementsWithErrors =
    sendFunctionAsync(GetPassportAuthorizationFormAvailableElements(authorizationFormId, password))

/**
 * Suspend function, which returns one of the available Telegram Passport elements.
 *
 * @param type Telegram Passport element type.
 * @param password Password of the current user.
 *
 * @return [PassportElement] This class is an abstract base class.
 */
suspend fun TelegramFlow.getPassportElement(
    type: PassportElementType?,
    password: String?
): PassportElement = sendFunctionAsync(GetPassportElement(type, password))

/**
 * Suspend function, which sends a Telegram Passport authorization form, effectively sharing data
 * with the service. This method must be called after getPassportAuthorizationFormAvailableElements if
 * some previously available elements need to be used.
 *
 * @param authorizationFormId Authorization form identifier.
 * @param types Types of Telegram Passport elements chosen by user to complete the authorization
 * form.
 */
suspend fun TelegramFlow.sendPassportAuthorizationForm(
    authorizationFormId: Int,
    types: Array<PassportElementType>?
) = sendFunctionLaunch(SendPassportAuthorizationForm(authorizationFormId, types))

/**
 * Suspend function, which adds an element to the user's Telegram Passport. May return an error with
 * a message &quot;PHONE_VERIFICATION_NEEDED&quot; or &quot;EMAIL_VERIFICATION_NEEDED&quot; if the
 * chosen phone number or the chosen email address must be verified first.
 *
 * @param element Input Telegram Passport element.
 * @param password Password of the current user.
 *
 * @return [PassportElement] This class is an abstract base class.
 */
suspend fun TelegramFlow.setPassportElement(
    element: InputPassportElement?,
    password: String?
): PassportElement = sendFunctionAsync(SetPassportElement(element, password))

/**
 * Suspend function, which informs the user that some of the elements in their Telegram Passport
 * contain errors; for bots only. The user will not be able to resend the elements, until the errors
 * are fixed.
 *
 * @param userId User identifier.
 * @param errors The errors.
 */
suspend fun TelegramFlow.setPassportElementErrors(
    userId: Int,
    errors: Array<InputPassportElementError>?
): Unit = sendFunctionLaunch(SetPassportElementErrors(userId, errors))