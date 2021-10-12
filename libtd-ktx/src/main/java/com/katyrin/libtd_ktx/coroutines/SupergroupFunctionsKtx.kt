package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which deletes a supergroup or channel along with all messages in the
 * corresponding chat. This will release the supergroup or channel username and remove all members;
 * requires owner privileges in the supergroup or channel. Chats with more than 1000 members can't be
 * deleted using this method.
 *
 * @param supergroupId Identifier of the supergroup or channel.
 */
suspend fun TelegramFlow.deleteSupergroup(supergroupId: Int): Unit =
    sendFunctionLaunch(DeleteSupergroup(supergroupId))

/**
 * Suspend function, which returns information about a supergroup or a channel by its identifier.
 * This is an offline request if the current user is not a bot.
 *
 * @param supergroupId Supergroup or channel identifier.
 *
 * @return [Supergroup] Represents a supergroup or channel with zero or more members (subscribers in
 * the case of channels). From the point of view of the system, a channel is a special kind of a
 * supergroup: only administrators can post and see the list of members, and posts from all
 * administrators use the name and photo of the channel instead of individual names and profile photos.
 * Unlike supergroups, channels can have an unlimited number of subscribers.
 */
suspend fun TelegramFlow.getSupergroup(supergroupId: Int): Supergroup =
    sendFunctionAsync(GetSupergroup(supergroupId))

/**
 * Suspend function, which returns full information about a supergroup or a channel by its
 * identifier, cached for up to 1 minute.
 *
 * @param supergroupId Supergroup or channel identifier.
 *
 * @return [SupergroupFullInfo] Contains full information about a supergroup or channel.
 */
suspend fun TelegramFlow.getSupergroupFullInfo(supergroupId: Int): SupergroupFullInfo =
    sendFunctionAsync(GetSupergroupFullInfo(supergroupId))

/**
 * Suspend function, which returns information about members or banned users in a supergroup or
 * channel. Can be used only if SupergroupFullInfo.canGetMembers == true; additionally, administrator
 * privileges may be required for some filters.
 *
 * @param supergroupId Identifier of the supergroup or channel.
 * @param filter The type of users to return. By default, supergroupMembersRecent.
 * @param offset Number of users to skip.
 * @param limit The maximum number of users be returned; up to 200.
 *
 * @return [ChatMembers] Contains a list of chat members.
 */
suspend fun TelegramFlow.getSupergroupMembers(
    supergroupId: Int,
    filter: SupergroupMembersFilter?,
    offset: Int,
    limit: Int
): ChatMembers = sendFunctionAsync(GetSupergroupMembers(supergroupId, filter, offset, limit))

/**
 * Suspend function, which reports some messages from a user in a supergroup as spam; requires
 * administrator rights in the supergroup.
 *
 * @param supergroupId Supergroup identifier.
 * @param userId User identifier.
 * @param messageIds Identifiers of messages sent in the supergroup by the user. This list must be
 * non-empty.
 */
suspend fun TelegramFlow.reportSupergroupSpam(
    supergroupId: Int,
    userId: Int,
    messageIds: LongArray?
): Unit = sendFunctionLaunch(ReportSupergroupSpam(supergroupId, userId, messageIds))

/**
 * Suspend function, which toggles whether the message history of a supergroup is available to new
 * members; requires canChangeInfo rights.
 *
 * @param supergroupId The identifier of the supergroup.
 * @param isAllHistoryAvailable The new value of isAllHistoryAvailable.
 */
suspend fun TelegramFlow.toggleSupergroupIsAllHistoryAvailable(
    supergroupId: Int,
    isAllHistoryAvailable: Boolean
): Unit = sendFunctionLaunch(
    ToggleSupergroupIsAllHistoryAvailable(supergroupId, isAllHistoryAvailable)
)