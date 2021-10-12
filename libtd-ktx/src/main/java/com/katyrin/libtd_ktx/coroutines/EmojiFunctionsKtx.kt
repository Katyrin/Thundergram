package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which returns an HTTP URL which can be used to automatically log in to the
 * translation platform and suggest new emoji replacements. The URL will be valid for 30 seconds after
 * generation.
 *
 * @param languageCode Language code for which the emoji replacements will be suggested.
 *
 * @return [HttpUrl] Contains an HTTP URL.
 */
suspend fun TelegramFlow.getEmojiSuggestionsUrl(languageCode: String?): HttpUrl =
    sendFunctionAsync(TdApi.GetEmojiSuggestionsUrl(languageCode))

/**
 * Suspend function, which returns emoji corresponding to a sticker. The list is only for
 * informational purposes, because a sticker is always sent with a fixed emoji from the corresponding
 * Sticker object.
 *
 * @param sticker Sticker file identifier.
 *
 * @return [Emojis] Represents a list of emoji.
 */
suspend fun TelegramFlow.getStickerEmojis(sticker: InputFile?): Emojis =
    sendFunctionAsync(TdApi.GetStickerEmojis(sticker))

/**
 * Suspend function, which searches for emojis by keywords. Supported only if the file database is
 * enabled.
 *
 * @param text Text to search for.
 * @param exactMatch True, if only emojis, which exactly match text needs to be returned.
 * @param inputLanguageCode IETF language tag of the user's input language; may be empty if unknown.
 *
 * @return [Emojis] Represents a list of emoji.
 */
suspend fun TelegramFlow.searchEmojis(
    text: String?,
    exactMatch: Boolean,
    inputLanguageCode: String?
): Emojis = sendFunctionAsync(TdApi.SearchEmojis(text, exactMatch, inputLanguageCode))