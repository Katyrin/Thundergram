package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which returns all entities (mentions, hashtags, cashtags, bot commands, URLs,
 * and email addresses) contained in the text. This is an offline method. Can be called before
 * authorization. Can be called synchronously.
 *
 * @param text The text in which to look for entites.
 *
 * @return [TextEntities] Contains a list of text entities.
 */
suspend fun TelegramFlow.getTextEntities(text: String?): TextEntities =
    sendFunctionAsync(GetTextEntities(text))

/**
 * Suspend function, which parses Bold, Italic, Underline, Strikethrough, Code, Pre, PreCode,
 * TextUrl and MentionName entities contained in the text. This is an offline method. Can be called
 * before authorization. Can be called synchronously.
 *
 * @param text The text which should be parsed.
 * @param parseMode Text parse mode.
 *
 * @return [FormattedText] A text with some entities.
 */
suspend fun TelegramFlow.parseTextEntities(
    text: String?,
    parseMode: TextParseMode?
): FormattedText = sendFunctionAsync(ParseTextEntities(text, parseMode))