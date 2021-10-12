package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Background
import org.drinkless.td.libcore.telegram.TdApi.BackgroundType
import org.drinkless.td.libcore.telegram.TdApi.Backgrounds
import org.drinkless.td.libcore.telegram.TdApi.HttpUrl
import org.drinkless.td.libcore.telegram.TdApi.InputBackground

/**
 * Suspend function, which constructs a persistent HTTP URL for a background.
 *
 * @param name Background name.
 * @param type Background type.
 *
 * @return [HttpUrl] Contains an HTTP URL.
 */
suspend fun TelegramFlow.getBackgroundUrl(name: String?, type: BackgroundType?): HttpUrl =
    sendFunctionAsync(TdApi.GetBackgroundUrl(name, type))

/**
 * Suspend function, which returns backgrounds installed by the user.
 *
 * @param forDarkTheme True, if the backgrounds needs to be ordered for dark theme.
 *
 * @return [Backgrounds] Contains a list of backgrounds.
 */
suspend fun TelegramFlow.getBackgrounds(forDarkTheme: Boolean): Backgrounds =
    sendFunctionAsync(TdApi.GetBackgrounds(forDarkTheme))

/**
 * Suspend function, which removes background from the list of installed backgrounds.
 *
 * @param backgroundId The background identifier.
 */
suspend fun TelegramFlow.removeBackground(backgroundId: Long): Unit =
    sendFunctionLaunch(TdApi.RemoveBackground(backgroundId))

/**
 * Suspend function, which resets list of installed backgrounds to its default value.
 */
suspend fun TelegramFlow.resetBackgrounds(): Unit = sendFunctionLaunch(TdApi.ResetBackgrounds())

/**
 * Suspend function, which searches for a background by its name.
 *
 * @param name The name of the background.
 *
 * @return [Background] Describes a chat background.
 */
suspend fun TelegramFlow.searchBackground(name: String?): Background =
    sendFunctionAsync(TdApi.SearchBackground(name))

/**
 * Suspend function, which changes the background selected by the user; adds background to the list
 * of installed backgrounds.
 *
 * @param background The input background to use, null for filled backgrounds.
 * @param type Background type; null for default background. The method will return error 404 if
 * type is null.
 * @param forDarkTheme True, if the background is chosen for dark theme.
 *
 * @return [Background] Describes a chat background.
 */
suspend fun TelegramFlow.setBackground(
    background: InputBackground?,
    type: BackgroundType?,
    forDarkTheme: Boolean
): Background = sendFunctionAsync(TdApi.SetBackground(background, type, forDarkTheme))
