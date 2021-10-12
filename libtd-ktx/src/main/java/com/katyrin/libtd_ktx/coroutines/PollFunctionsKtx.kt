package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.ReplyMarkup
import org.drinkless.td.libcore.telegram.TdApi.Users

/**
 * Suspend function, which returns users voted for the specified option in a non-anonymous polls.
 * For the optimal performance the number of returned users is chosen by the library.
 *
 * @param chatId Identifier of the chat to which the poll belongs.
 * @param messageId Identifier of the message containing the poll.
 * @param optionId 0-based identifier of the answer option.
 * @param offset Number of users to skip in the result; must be non-negative.
 * @param limit The maximum number of users to be returned; must be positive and can't be greater
 * than 50. Fewer users may be returned than specified by the limit, even if the end of the voter list
 * has not been reached.
 *
 * @return [Users] Represents a list of users.
 */
suspend fun TelegramFlow.getPollVoters(
    chatId: Long,
    messageId: Long,
    optionId: Int,
    offset: Int,
    limit: Int
): Users = sendFunctionAsync(TdApi.GetPollVoters(chatId, messageId, optionId, offset, limit))

/**
 * Suspend function, which changes the user answer to a poll. A poll in quiz mode can be answered
 * only once.
 *
 * @param chatId Identifier of the chat to which the poll belongs.
 * @param messageId Identifier of the message containing the poll.
 * @param optionIds 0-based identifiers of answer options, chosen by the user. User can choose more
 * than 1 answer option only is the poll allows multiple answers.
 */
suspend fun TelegramFlow.setPollAnswer(
    chatId: Long,
    messageId: Long,
    optionIds: IntArray?
): Unit = sendFunctionLaunch(TdApi.SetPollAnswer(chatId, messageId, optionIds))

/**
 * Suspend function, which stops a poll. A poll in a message can be stopped when the message has
 * canBeEdited flag set.
 *
 * @param chatId Identifier of the chat to which the poll belongs.
 * @param messageId Identifier of the message containing the poll.
 * @param replyMarkup The new message reply markup; for bots only.
 */
suspend fun TelegramFlow.stopPoll(
    chatId: Long,
    messageId: Long,
    replyMarkup: ReplyMarkup?
): Unit = sendFunctionLaunch(TdApi.StopPoll(chatId, messageId, replyMarkup))