package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.OrderInfo
import org.drinkless.td.libcore.telegram.TdApi.ValidatedOrderInfo

/**
 * Suspend function, which deletes saved order info.
 */
suspend fun TelegramFlow.deleteSavedOrderInfo(): Unit =
    sendFunctionLaunch(TdApi.DeleteSavedOrderInfo())

/**
 * Suspend function, which returns saved order info, if any.
 *
 * @return [OrderInfo] Order information.
 */
suspend fun TelegramFlow.getSavedOrderInfo(): OrderInfo =
    sendFunctionAsync(TdApi.GetSavedOrderInfo())

/**
 * Suspend function, which validates the order information provided by a user and returns the
 * available shipping options for a flexible invoice.
 *
 * @param chatId Chat identifier of the Invoice message.
 * @param messageId Message identifier.
 * @param orderInfo The order information, provided by the user.
 * @param allowSave True, if the order information can be saved.
 *
 * @return [ValidatedOrderInfo] Contains a temporary identifier of validated order information,
 * which is stored for one hour. Also contains the available shipping options.
 */
suspend fun TelegramFlow.validateOrderInfo(
    chatId: Long,
    messageId: Long,
    orderInfo: OrderInfo?,
    allowSave: Boolean
): ValidatedOrderInfo =
    sendFunctionAsync(TdApi.ValidateOrderInfo(chatId, messageId, orderInfo, allowSave))