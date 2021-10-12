package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which adds a new sticker to the list of favorite stickers. The new sticker is
 * added to the top of the list. If the sticker was already in the list, it is removed from the list
 * first. Only stickers belonging to a sticker set can be added to this list.
 *
 * @param sticker Sticker file to add.
 */
suspend fun TelegramFlow.addFavoriteSticker(sticker: InputFile?): Unit =
    sendFunctionLaunch(AddFavoriteSticker(sticker))

/**
 * Suspend function, which manually adds a new sticker to the list of recently used stickers. The
 * new sticker is added to the top of the list. If the sticker was already in the list, it is removed
 * from the list first. Only stickers belonging to a sticker set can be added to this list.
 *
 * @param isAttached Pass true to add the sticker to the list of stickers recently attached to photo
 * or video files; pass false to add the sticker to the list of recently sent stickers.
 * @param sticker Sticker file to add.
 *
 * @return [Stickers] Represents a list of stickers.
 */
suspend fun TelegramFlow.addRecentSticker(isAttached: Boolean, sticker: InputFile?): Stickers =
    sendFunctionAsync(AddRecentSticker(isAttached, sticker))

/**
 * Suspend function, which adds a new sticker to a set; for bots only. Returns the sticker set.
 *
 * @param userId Sticker set owner.
 * @param name Sticker set name.
 * @param sticker Sticker to add to the set.
 *
 * @return [StickerSet] Represents a sticker set.
 */
suspend fun TelegramFlow.addStickerToSet(
    userId: Int,
    name: String?,
    sticker: InputSticker?
): StickerSet = sendFunctionAsync(AddStickerToSet(userId, name, sticker))

/**
 * Suspend function, which installs/uninstalls or activates/archives a sticker set.
 *
 * @param setId Identifier of the sticker set.
 * @param isInstalled The new value of isInstalled.
 * @param isArchived The new value of isArchived. A sticker set can't be installed and archived
 * simultaneously.
 */
suspend fun TelegramFlow.changeStickerSet(
    setId: Long,
    isInstalled: Boolean,
    isArchived: Boolean
): Unit = sendFunctionLaunch(ChangeStickerSet(setId, isInstalled, isArchived))

/**
 * Suspend function, which clears the list of recently used stickers.
 *
 * @param isAttached Pass true to clear the list of stickers recently attached to photo or video
 * files; pass false to clear the list of recently sent stickers.
 */
suspend fun TelegramFlow.clearRecentStickers(isAttached: Boolean): Unit =
    sendFunctionLaunch(ClearRecentStickers(isAttached))

/**
 * Suspend function, which creates a new sticker set; for bots only. Returns the newly created
 * sticker set.
 *
 * @param userId Sticker set owner.
 * @param title Sticker set title; 1-64 characters.
 * @param name Sticker set name. Can contain only English letters, digits and underscores. Must end
 * with *&quot;_by_&lt;bot username&gt;&quot;* (*&lt;botUsername&gt;* is case insensitive); 1-64
 * characters.
 * @param isMasks True, if stickers are masks.
 * @param stickers List of stickers to be added to the set.
 *
 * @return [StickerSet] Represents a sticker set.
 */
suspend fun TelegramFlow.createNewStickerSet(
    userId: Int,
    title: String?,
    name: String?,
    isMasks: Boolean,
    stickers: Array<InputSticker>?
): StickerSet = sendFunctionAsync(CreateNewStickerSet(userId, title, name, isMasks, stickers))

/**
 * Suspend function, which returns a list of archived sticker sets.
 *
 * @param isMasks Pass true to return mask stickers sets; pass false to return ordinary sticker
 * sets.
 * @param offsetStickerSetId Identifier of the sticker set from which to return the result.
 * @param limit The maximum number of sticker sets to return.
 *
 * @return [StickerSets] Represents a list of sticker sets.
 */
suspend fun TelegramFlow.getArchivedStickerSets(
    isMasks: Boolean,
    offsetStickerSetId: Long,
    limit: Int
): StickerSets = sendFunctionAsync(GetArchivedStickerSets(isMasks, offsetStickerSetId, limit))

/**
 * Suspend function, which returns a list of sticker sets attached to a file. Currently only photos
 * and videos can have attached sticker sets.
 *
 * @param fileId File identifier.
 *
 * @return [StickerSets] Represents a list of sticker sets.
 */
suspend fun TelegramFlow.getAttachedStickerSets(fileId: Int): StickerSets =
    sendFunctionAsync(GetAttachedStickerSets(fileId))

/**
 * Suspend function, which returns favorite stickers.
 *
 * @return [Stickers] Represents a list of stickers.
 */
suspend fun TelegramFlow.getFavoriteStickers(): Stickers = sendFunctionAsync(GetFavoriteStickers())

/**
 * Suspend function, which returns a list of installed sticker sets.
 *
 * @param isMasks Pass true to return mask sticker sets; pass false to return ordinary sticker sets.
 *
 * @return [StickerSets] Represents a list of sticker sets.
 */
suspend fun TelegramFlow.getInstalledStickerSets(isMasks: Boolean): StickerSets =
    sendFunctionAsync(GetInstalledStickerSets(isMasks))

/**
 * Suspend function, which returns a list of recently used stickers.
 *
 * @param isAttached Pass true to return stickers and masks that were recently attached to photos or
 * video files; pass false to return recently sent stickers.
 *
 * @return [Stickers] Represents a list of stickers.
 */
suspend fun TelegramFlow.getRecentStickers(isAttached: Boolean): Stickers =
    sendFunctionAsync(GetRecentStickers(isAttached))

/**
 * Suspend function, which returns information about a sticker set by its identifier.
 *
 * @param setId Identifier of the sticker set.
 *
 * @return [StickerSet] Represents a sticker set.
 */
suspend fun TelegramFlow.getStickerSet(setId: Long): StickerSet =
    sendFunctionAsync(GetStickerSet(setId))

/**
 * Suspend function, which returns stickers from the installed sticker sets that correspond to a
 * given emoji. If the emoji is not empty, favorite and recently used stickers may also be returned.
 *
 * @param emoji String representation of emoji. If empty, returns all known installed stickers.
 * @param limit The maximum number of stickers to be returned.
 *
 * @return [Stickers] Represents a list of stickers.
 */
suspend fun TelegramFlow.getStickers(emoji: String?, limit: Int): Stickers =
    sendFunctionAsync(GetStickers(emoji, limit))

/**
 * Suspend function, which returns a list of trending sticker sets.
 *
 * @return [StickerSets] Represents a list of sticker sets.
 */
suspend fun TelegramFlow.getTrendingStickerSets(): StickerSets =
    sendFunctionAsync(GetTrendingStickerSets())

/**
 * Suspend function, which removes a sticker from the list of favorite stickers.
 *
 * @param sticker Sticker file to delete from the list.
 */
suspend fun TelegramFlow.removeFavoriteSticker(sticker: InputFile?): Unit =
    sendFunctionLaunch(RemoveFavoriteSticker(sticker))

/**
 * Suspend function, which removes a sticker from the list of recently used stickers.
 *
 * @param isAttached Pass true to remove the sticker from the list of stickers recently attached to
 * photo or video files; pass false to remove the sticker from the list of recently sent stickers.
 * @param sticker Sticker file to delete.
 */
suspend fun TelegramFlow.removeRecentSticker(isAttached: Boolean, sticker: InputFile?): Unit =
    sendFunctionLaunch(RemoveRecentSticker(isAttached, sticker))

/**
 * Suspend function, which removes a sticker from the set to which it belongs; for bots only. The
 * sticker set must have been created by the bot.
 *
 * @param sticker Sticker.
 */
suspend fun TelegramFlow.removeStickerFromSet(sticker: InputFile?): Unit =
    sendFunctionLaunch(RemoveStickerFromSet(sticker))

/**
 * Suspend function, which changes the order of installed sticker sets.
 *
 * @param isMasks Pass true to change the order of mask sticker sets; pass false to change the order
 * of ordinary sticker sets.
 * @param stickerSetIds Identifiers of installed sticker sets in the new correct order.
 */
suspend fun TelegramFlow.reorderInstalledStickerSets(
    isMasks: Boolean,
    stickerSetIds: LongArray?
): Unit = sendFunctionLaunch(ReorderInstalledStickerSets(isMasks, stickerSetIds))

/**
 * Suspend function, which searches for installed sticker sets by looking for specified query in
 * their title and name.
 *
 * @param isMasks Pass true to return mask sticker sets; pass false to return ordinary sticker sets.
 *
 * @param query Query to search for.
 * @param limit The maximum number of sticker sets to return.
 *
 * @return [StickerSets] Represents a list of sticker sets.
 */
suspend fun TelegramFlow.searchInstalledStickerSets(
    isMasks: Boolean,
    query: String?,
    limit: Int
): StickerSets = sendFunctionAsync(SearchInstalledStickerSets(isMasks, query, limit))

/**
 * Suspend function, which searches for a sticker set by its name.
 *
 * @param name Name of the sticker set.
 *
 * @return [StickerSet] Represents a sticker set.
 */
suspend fun TelegramFlow.searchStickerSet(name: String?): StickerSet =
    sendFunctionAsync(SearchStickerSet(name))

/**
 * Suspend function, which searches for ordinary sticker sets by looking for specified query in
 * their title and name. Excludes installed sticker sets from the results.
 *
 * @param query Query to search for.
 *
 * @return [StickerSets] Represents a list of sticker sets.
 */
suspend fun TelegramFlow.searchStickerSets(query: String?): StickerSets =
    sendFunctionAsync(SearchStickerSets(query))

/**
 * Suspend function, which searches for stickers from public sticker sets that correspond to a given
 * emoji.
 *
 * @param emoji String representation of emoji; must be non-empty.
 * @param limit The maximum number of stickers to be returned.
 *
 * @return [Stickers] Represents a list of stickers.
 */
suspend fun TelegramFlow.searchStickers(emoji: String?, limit: Int): Stickers =
    sendFunctionAsync(SearchStickers(emoji, limit))

/**
 * Suspend function, which changes the position of a sticker in the set to which it belongs; for
 * bots only. The sticker set must have been created by the bot.
 *
 * @param sticker Sticker.
 * @param position New position of the sticker in the set, zero-based.
 */
suspend fun TelegramFlow.setStickerPositionInSet(sticker: InputFile?, position: Int): Unit =
    sendFunctionLaunch(SetStickerPositionInSet(sticker, position))

/**
 * Suspend function, which changes the sticker set of a supergroup; requires canChangeInfo rights.
 *
 * @param supergroupId Identifier of the supergroup.
 * @param stickerSetId New value of the supergroup sticker set identifier. Use 0 to remove the
 * supergroup sticker set.
 */
suspend fun TelegramFlow.setSupergroupStickerSet(supergroupId: Int, stickerSetId: Long): Unit =
    sendFunctionLaunch(SetSupergroupStickerSet(supergroupId, stickerSetId))

/**
 * Suspend function, which informs the server that some trending sticker sets have been viewed by
 * the user.
 *
 * @param stickerSetIds Identifiers of viewed trending sticker sets.
 */
suspend fun TelegramFlow.viewTrendingStickerSets(stickerSetIds: LongArray?): Unit =
    sendFunctionLaunch(ViewTrendingStickerSets(stickerSetIds))