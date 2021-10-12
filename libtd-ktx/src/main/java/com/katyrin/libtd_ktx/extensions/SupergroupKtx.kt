package com.katyrin.libtd_ktx.extensions

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.*
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Supergroup
import org.drinkless.td.libcore.telegram.TdApi.SupergroupMembersFilter

/**
 * Interface for access [TdApi.Supergroup] extension functions. Can be used alongside with other
 * extension interfaces of the package. Must contain [TelegramFlow] instance field to access its
 * functionality
 */
interface SupergroupKtx : BaseKtx {
    /**
     * Instance of the [TelegramFlow] connecting extensions to the Telegram Client
     */
    override val api: TelegramFlow

    /**
     * Suspend function, which returns an existing chat corresponding to a known supergroup or
     * channel.
     *
     * @param force If true, the chat will be created without network request. In this case all
     * information about the chat except its type, title and photo can be incorrect.
     *
     * @return [TdApi.Chat] A chat. (Can be a private chat, basic group, supergroup, or secret chat.)
     */
    suspend fun Supergroup.createChat(force: Boolean): TdApi.Chat =
        api.createSupergroupChat(id, force)

    /**
     * Suspend function, which deletes a supergroup or channel along with all messages in the
     * corresponding chat. This will release the supergroup or channel username and remove all members;
     * requires owner privileges in the supergroup or channel. Chats with more than 1000 members can't be
     * deleted using this method.
     */
    suspend fun Supergroup.delete(): Unit = api.deleteSupergroup(id)

    /**
     * Suspend function, which returns information about a supergroup or a channel by its identifier.
     * This is an offline request if the current user is not a bot.
     *
     *
     * @return [TdApi.Supergroup] Represents a supergroup or channel with zero or more members
     * (subscribers in the case of channels). From the point of view of the system, a channel is a
     * special kind of a supergroup: only administrators can post and see the list of members, and posts
     * from all administrators use the name and photo of the channel instead of individual names and
     * profile photos. Unlike supergroups, channels can have an unlimited number of subscribers.
     */
    suspend fun Supergroup.get(): Supergroup = api.getSupergroup(id)

    /**
     * Suspend function, which returns full information about a supergroup or a channel by its
     * identifier, cached for up to 1 minute.
     *
     *
     * @return [TdApi.SupergroupFullInfo] Contains full information about a supergroup or channel.
     */
    suspend fun Supergroup.getFullInfo(): TdApi.SupergroupFullInfo = api.getSupergroupFullInfo(id)

    /**
     * Suspend function, which returns information about members or banned users in a supergroup or
     * channel. Can be used only if SupergroupFullInfo.canGetMembers == true; additionally, administrator
     * privileges may be required for some filters.
     *
     * @param filter The type of users to return. By default, supergroupMembersRecent.
     * @param offset Number of users to skip.
     * @param limit The maximum number of users be returned; up to 200.
     *
     * @return [TdApi.ChatMembers] Contains a list of chat members.
     */
    suspend fun Supergroup.getMembers(
        filter: SupergroupMembersFilter?,
        offset: Int,
        limit: Int
    ): TdApi.ChatMembers = api.getSupergroupMembers(id, filter, offset, limit)

    /**
     * Suspend function, which reports some messages from a user in a supergroup as spam; requires
     * administrator rights in the supergroup.
     *
     * @param userId User identifier.
     * @param messageIds Identifiers of messages sent in the supergroup by the user. This list must be
     * non-empty.
     */
    suspend fun Supergroup.reportSpam(userId: Int, messageIds: LongArray?): Unit =
        api.reportSupergroupSpam(id, userId, messageIds)

    /**
     * Suspend function, which changes the sticker set of a supergroup; requires canChangeInfo rights.
     *
     * @param stickerSetId New value of the supergroup sticker set identifier. Use 0 to remove the
     * supergroup sticker set.
     */
    suspend fun Supergroup.setStickerSet(stickerSetId: Long): Unit =
        api.setSupergroupStickerSet(id, stickerSetId)

    /**
     * Suspend function, which changes the username of a supergroup or channel, requires owner
     * privileges in the supergroup or channel.
     *
     * @param username New value of the username. Use an empty string to remove the username.
     */
    suspend fun Supergroup.setUsername(username: String?): Unit =
        api.setSupergroupUsername(id, username)

    /**
     * Suspend function, which toggles whether the message history of a supergroup is available to new
     * members; requires canChangeInfo rights.
     *
     * @param isAllHistoryAvailable The new value of isAllHistoryAvailable.
     */
    suspend fun Supergroup.toggleIsAllHistoryAvailable(isAllHistoryAvailable: Boolean): Unit =
        api.toggleSupergroupIsAllHistoryAvailable(id, isAllHistoryAvailable)

    /**
     * Suspend function, which toggles sender signatures messages sent in a channel; requires
     * canChangeInfo rights.
     *
     * @param signMessages New value of signMessages.
     */
    suspend fun Supergroup.toggleSignMessages(signMessages: Boolean): Unit =
        api.toggleSupergroupSignMessages(id, signMessages)
}