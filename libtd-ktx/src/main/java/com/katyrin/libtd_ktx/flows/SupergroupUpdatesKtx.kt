package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Supergroup
import org.drinkless.td.libcore.telegram.TdApi.UpdateSupergroupFullInfo

/**
 * emits [Supergroup] if some data of a supergroup or a channel has changed. This update is
 * guaranteed to come before the supergroup identifier is returned to the client.
 */
fun TelegramFlow.supergroupFlow(): Flow<Supergroup> =
    getUpdatesFlowOfType<TdApi.UpdateSupergroup>().mapNotNull { it.supergroup }

/**
 * emits [UpdateSupergroupFullInfo] if some data from supergroupFullInfo has been changed.
 */
fun TelegramFlow.supergroupFullInfoFlow(): Flow<UpdateSupergroupFullInfo> = getUpdatesFlowOfType()