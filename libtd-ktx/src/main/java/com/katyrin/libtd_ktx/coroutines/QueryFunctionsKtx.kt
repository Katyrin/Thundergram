package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which sets the result of a callback query; for bots only.
 *
 * @param callbackQueryId Identifier of the callback query.
 * @param text Text of the answer.
 * @param showAlert If true, an alert should be shown to the user instead of a toast notification.
 * @param url URL to be opened.
 * @param cacheTime Time during which the result of the query can be cached, in seconds.
 */
suspend fun TelegramFlow.answerCallbackQuery(
    callbackQueryId: Long,
    text: String?,
    showAlert: Boolean,
    url: String?,
    cacheTime: Int
): Unit = sendFunctionLaunch(AnswerCallbackQuery(callbackQueryId, text, showAlert, url, cacheTime))

/**
 * Suspend function, which answers a custom query; for bots only.
 *
 * @param customQueryId Identifier of a custom query.
 * @param data JSON-serialized answer to the query.
 */
suspend fun TelegramFlow.answerCustomQuery(customQueryId: Long, data: String?): Unit =
    sendFunctionLaunch(AnswerCustomQuery(customQueryId, data))

/**
 * Suspend function, which sets the result of an inline query; for bots only.
 *
 * @param inlineQueryId Identifier of the inline query.
 * @param isPersonal True, if the result of the query can be cached for the specified user.
 * @param results The results of the query.
 * @param cacheTime Allowed time to cache the results of the query, in seconds.
 * @param nextOffset Offset for the next inline query; pass an empty string if there are no more
 * results.
 * @param switchPmText If non-empty, this text should be shown on the button that opens a private
 * chat with the bot and sends a start message to the bot with the parameter switchPmParameter.
 * @param switchPmParameter The parameter for the bot start message.
 */
suspend fun TelegramFlow.answerInlineQuery(
    inlineQueryId: Long,
    isPersonal: Boolean,
    results: Array<InputInlineQueryResult>?,
    cacheTime: Int,
    nextOffset: String?,
    switchPmText: String?,
    switchPmParameter: String?
): Unit = sendFunctionLaunch(
    AnswerInlineQuery(
        inlineQueryId,
        isPersonal,
        results,
        cacheTime,
        nextOffset,
        switchPmText,
        switchPmParameter
    )
)

/**
 * Suspend function, which sets the result of a pre-checkout query; for bots only.
 *
 * @param preCheckoutQueryId Identifier of the pre-checkout query.
 * @param errorMessage An error message, empty on success.
 */
suspend fun TelegramFlow.answerPreCheckoutQuery(
    preCheckoutQueryId: Long,
    errorMessage: String?
): Unit = sendFunctionLaunch(AnswerPreCheckoutQuery(preCheckoutQueryId, errorMessage))

/**
 * Suspend function, which sets the result of a shipping query; for bots only.
 *
 * @param shippingQueryId Identifier of the shipping query.
 * @param shippingOptions Available shipping options.
 * @param errorMessage An error message, empty on success.
 */
suspend fun TelegramFlow.answerShippingQuery(
    shippingQueryId: Long,
    shippingOptions: Array<ShippingOption>?,
    errorMessage: String?
): Unit = sendFunctionLaunch(AnswerShippingQuery(shippingQueryId, shippingOptions, errorMessage))

/**
 * Suspend function, which sends a callback query to a bot and returns an answer. Returns an error
 * with code 502 if the bot fails to answer the query before the query timeout expires.
 *
 * @param chatId Identifier of the chat with the message.
 * @param messageId Identifier of the message from which the query originated.
 * @param payload Query payload.
 *
 * @return [CallbackQueryAnswer] Contains a bot's answer to a callback query.
 */
suspend fun TelegramFlow.getCallbackQueryAnswer(
    chatId: Long,
    messageId: Long,
    payload: CallbackQueryPayload?
): CallbackQueryAnswer = sendFunctionAsync(GetCallbackQueryAnswer(chatId, messageId, payload))

/**
 * Suspend function, which sends an inline query to a bot and returns its results. Returns an error
 * with code 502 if the bot fails to answer the query before the query timeout expires.
 *
 * @param botUserId The identifier of the target bot.
 * @param chatId Identifier of the chat where the query was sent.
 * @param userLocation Location of the user, only if needed.
 * @param query Text of the query.
 * @param offset Offset of the first entry to return.
 *
 * @return [InlineQueryResults] Represents the results of the inline query. Use
 * sendInlineQueryResultMessage to send the result of the query.
 */
suspend fun TelegramFlow.getInlineQueryResults(
    botUserId: Int,
    chatId: Long,
    userLocation: Location?,
    query: String?,
    offset: String?
): InlineQueryResults = sendFunctionAsync(
    GetInlineQueryResults(botUserId, chatId, userLocation, query, offset)
)

/**
 * Suspend function, which sends the result of an inline query as a message. Returns the sent
 * message. Always clears a chat draft message.
 *
 * @param chatId Target chat.
 * @param replyToMessageId Identifier of a message to reply to or 0.
 * @param options Options to be used to send the message.
 * @param queryId Identifier of the inline query.
 * @param resultId Identifier of the inline result.
 * @param hideViaBot If true, there will be no mention of a bot, via which the message is sent. Can
 * be used only for bots GetOption(&quot;animation_search_bot_username&quot;),
 * GetOption(&quot;photo_search_bot_username&quot;) and
 * GetOption(&quot;venue_search_bot_username&quot;).
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.sendInlineQueryResultMessage(
    chatId: Long,
    replyToMessageId: Long,
    options: SendMessageOptions?,
    queryId: Long,
    resultId: String?,
    hideViaBot: Boolean
): Message = sendFunctionAsync(
    SendInlineQueryResultMessage(chatId, replyToMessageId, options, queryId, resultId, hideViaBot)
)