package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlin.IntArray
import kotlin.String
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationState
import org.drinkless.td.libcore.telegram.TdApi.ConnectionState
import org.drinkless.td.libcore.telegram.TdApi.UpdateLanguagePackStrings
import org.drinkless.td.libcore.telegram.TdApi.UpdateNewChosenInlineResult
import org.drinkless.td.libcore.telegram.TdApi.UpdateNewCustomQuery
import org.drinkless.td.libcore.telegram.TdApi.UpdateNewInlineQuery
import org.drinkless.td.libcore.telegram.TdApi.UpdateNewPreCheckoutQuery
import org.drinkless.td.libcore.telegram.TdApi.UpdateNewShippingQuery
import org.drinkless.td.libcore.telegram.TdApi.UpdateOption
import org.drinkless.td.libcore.telegram.TdApi.UpdateSelectedBackground
import org.drinkless.td.libcore.telegram.TdApi.UpdateTermsOfService

/**
 * emits [AuthorizationState] if the user authorization state has changed.
 */
fun TelegramFlow.authorizationStateFlow(): Flow<AuthorizationState> =
    getUpdatesFlowOfType<TdApi.UpdateAuthorizationState>().mapNotNull { it.authorizationState }

/**
 * emits [UpdateOption] if an option changed its value.
 */
fun TelegramFlow.optionFlow(): Flow<UpdateOption> = getUpdatesFlowOfType()

/**
 * emits animationIds [Int[]] if the list of saved animations was updated.
 */
fun TelegramFlow.savedAnimationsFlow(): Flow<IntArray> =
    getUpdatesFlowOfType<TdApi.UpdateSavedAnimations>().mapNotNull { it.animationIds }

/**
 * emits [UpdateSelectedBackground] if the selected background has changed.
 */
fun TelegramFlow.selectedBackgroundFlow(): Flow<UpdateSelectedBackground> = getUpdatesFlowOfType()

/**
 * emits [UpdateLanguagePackStrings] if some language pack strings have been updated.
 */
fun TelegramFlow.languagePackStringsFlow(): Flow<UpdateLanguagePackStrings> = getUpdatesFlowOfType()

/**
 * emits state [ConnectionState] if the connection state has changed.
 */
fun TelegramFlow.connectionStateFlow(): Flow<ConnectionState> =
    getUpdatesFlowOfType<TdApi.UpdateConnectionState>().mapNotNull { it.state }

/**
 * emits [UpdateTermsOfService] if new terms of service must be accepted by the user. If the terms
 * of service are declined, then the deleteAccount method should be called with the reason
 * &quot;Decline ToS update&quot;.
 */
fun TelegramFlow.termsOfServiceFlow(): Flow<UpdateTermsOfService> = getUpdatesFlowOfType()

/**
 * emits [UpdateNewInlineQuery] if a new incoming inline query; for bots only.
 */
fun TelegramFlow.newInlineQueryFlow(): Flow<UpdateNewInlineQuery> = getUpdatesFlowOfType()

/**
 * emits [UpdateNewChosenInlineResult] if the user has chosen a result of an inline query; for bots
 * only.
 */
fun TelegramFlow.newChosenInlineResultFlow(): Flow<UpdateNewChosenInlineResult> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateNewShippingQuery] if a new incoming shipping query; for bots only. Only for invoices
 * with flexible price.
 */
fun TelegramFlow.newShippingQueryFlow(): Flow<UpdateNewShippingQuery> = getUpdatesFlowOfType()

/**
 * emits [UpdateNewPreCheckoutQuery] if a new incoming pre-checkout query; for bots only. Contains
 * full information about a checkout.
 */
fun TelegramFlow.newPreCheckoutQueryFlow(): Flow<UpdateNewPreCheckoutQuery> = getUpdatesFlowOfType()

/**
 * emits event [String] if a new incoming event; for bots only.
 */
fun TelegramFlow.newCustomEventFlow(): Flow<String> =
    getUpdatesFlowOfType<TdApi.UpdateNewCustomEvent>().mapNotNull { it.event }

/**
 * emits [UpdateNewCustomQuery] if a new incoming query; for bots only.
 */
fun TelegramFlow.newCustomQueryFlow(): Flow<UpdateNewCustomQuery> = getUpdatesFlowOfType()