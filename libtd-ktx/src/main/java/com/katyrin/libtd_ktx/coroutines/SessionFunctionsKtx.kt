package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Sessions

/**
 * Suspend function, which returns all active sessions of the current user.
 *
 * @return [Sessions] Contains a list of sessions.
 */
suspend fun TelegramFlow.getActiveSessions(): Sessions =
    sendFunctionAsync(TdApi.GetActiveSessions())

/**
 * Suspend function, which terminates all other sessions of the current user.
 */
suspend fun TelegramFlow.terminateAllOtherSessions(): Unit =
    sendFunctionLaunch(TdApi.TerminateAllOtherSessions())

/**
 * Suspend function, which terminates a session of the current user.
 *
 * @param sessionId Session identifier.
 */
suspend fun TelegramFlow.terminateSession(sessionId: Long): Unit =
    sendFunctionLaunch(TdApi.TerminateSession(sessionId))