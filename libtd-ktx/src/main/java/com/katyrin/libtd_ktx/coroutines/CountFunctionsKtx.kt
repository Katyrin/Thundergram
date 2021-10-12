package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Count
import org.drinkless.td.libcore.telegram.TdApi.Text

/**
 * Suspend function, which uses current user IP to found their country. Returns two-letter ISO
 * 3166-1 alpha-2 country code. Can be called before authorization.
 *
 * @return [Text] Contains some text.
 */
suspend fun TelegramFlow.getCountryCode(): Text = sendFunctionAsync(TdApi.GetCountryCode())

/**
 * Suspend function, which returns the total number of imported contacts.
 *
 * @return [Count] Contains a counter.
 */
suspend fun TelegramFlow.getImportedContactCount(): Count =
    sendFunctionAsync(TdApi.GetImportedContactCount())