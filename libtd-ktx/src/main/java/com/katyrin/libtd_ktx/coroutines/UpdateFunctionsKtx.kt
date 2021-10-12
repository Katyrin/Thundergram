package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Update

/**
 * Suspend function, which informs the server about the number of pending bot updates if they
 * haven't been processed for a long time; for bots only.
 *
 * @param pendingUpdateCount The number of pending updates.
 * @param errorMessage The last error message.
 */
suspend fun TelegramFlow.setBotUpdatesStatus(pendingUpdateCount: Int, errorMessage: String?): Unit =
    sendFunctionLaunch(TdApi.SetBotUpdatesStatus(pendingUpdateCount, errorMessage))

/**
 * Suspend function, which does nothing and ensures that the Update object is used; for testing
 * only. This is an offline method. Can be called before authorization.
 *
 * @return [Update] This class is an abstract base class.
 */
suspend fun TelegramFlow.testUseUpdate(): Update = sendFunctionAsync(TdApi.TestUseUpdate())