package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Call
import org.drinkless.td.libcore.telegram.TdApi.UpdateNewCallbackQuery
import org.drinkless.td.libcore.telegram.TdApi.UpdateNewInlineCallbackQuery

/**
 * emits [Call] if new call was created or information about a call was updated.
 */
fun TelegramFlow.callFlow(): Flow<Call> =
    getUpdatesFlowOfType<TdApi.UpdateCall>().mapNotNull { it.call }

/**
 * emits [UpdateNewCallbackQuery] if a new incoming callback query; for bots only.
 */
fun TelegramFlow.newCallbackQueryFlow(): Flow<UpdateNewCallbackQuery> = getUpdatesFlowOfType()

/**
 * emits [UpdateNewInlineCallbackQuery] if a new incoming callback query from a message sent via a
 * bot; for bots only.
 */
fun TelegramFlow.newInlineCallbackQueryFlow(): Flow<UpdateNewInlineCallbackQuery> =
    getUpdatesFlowOfType()
