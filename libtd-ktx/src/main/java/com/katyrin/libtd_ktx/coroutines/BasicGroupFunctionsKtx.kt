package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlin.Int
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.BasicGroup
import org.drinkless.td.libcore.telegram.TdApi.BasicGroupFullInfo

/**
 * Suspend function, which returns information about a basic group by its identifier. This is an
 * offline request if the current user is not a bot.
 *
 * @param basicGroupId Basic group identifier.
 *
 * @return [BasicGroup] Represents a basic group of 0-200 users (must be upgraded to a supergroup to
 * accommodate more than 200 users).
 */
suspend fun TelegramFlow.getBasicGroup(basicGroupId: Int): BasicGroup =
    sendFunctionAsync(TdApi.GetBasicGroup(basicGroupId))

/**
 * Suspend function, which returns full information about a basic group by its identifier.
 *
 * @param basicGroupId Basic group identifier.
 *
 * @return [BasicGroupFullInfo] Contains full information about a basic group.
 */
suspend fun TelegramFlow.getBasicGroupFullInfo(basicGroupId: Int): BasicGroupFullInfo =
    sendFunctionAsync(TdApi.GetBasicGroupFullInfo(basicGroupId))