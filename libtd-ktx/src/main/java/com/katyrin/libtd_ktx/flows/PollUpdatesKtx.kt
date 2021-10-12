package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Poll
import org.drinkless.td.libcore.telegram.TdApi.UpdatePollAnswer

/**
 * emits [Poll] if a poll was updated; for bots only.
 */
fun TelegramFlow.pollFlow(): Flow<Poll> =
    getUpdatesFlowOfType<TdApi.UpdatePoll>().mapNotNull { it.poll }

/**
 * emits [UpdatePollAnswer] if a user changed the answer to a poll; for bots only.
 */
fun TelegramFlow.pollAnswerFlow(): Flow<UpdatePollAnswer> = getUpdatesFlowOfType()