package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.BasicGroup
import org.drinkless.td.libcore.telegram.TdApi.UpdateBasicGroupFullInfo

/**
 * emits [BasicGroup] if some data of a basic group has changed. This update is guaranteed to come
 * before the basic group identifier is returned to the client.
 */
fun TelegramFlow.basicGroupFlow(): Flow<BasicGroup> =
    getUpdatesFlowOfType<TdApi.UpdateBasicGroup>().mapNotNull { it.basicGroup }

/**
 * emits [UpdateBasicGroupFullInfo] if some data from basicGroupFullInfo has been changed.
 */
fun TelegramFlow.basicGroupFullInfoFlow(): Flow<UpdateBasicGroupFullInfo> = getUpdatesFlowOfType()
