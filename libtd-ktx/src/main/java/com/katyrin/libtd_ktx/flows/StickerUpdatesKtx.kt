package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlin.IntArray
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.StickerSets
import org.drinkless.td.libcore.telegram.TdApi.UpdateInstalledStickerSets
import org.drinkless.td.libcore.telegram.TdApi.UpdateRecentStickers

/**
 * emits [UpdateInstalledStickerSets] if the list of installed sticker sets was updated.
 */
fun TelegramFlow.installedStickerSetsFlow(): Flow<UpdateInstalledStickerSets> =
    getUpdatesFlowOfType()

/**
 * emits [StickerSets] if the list of trending sticker sets was updated or some of them were viewed.
 */
fun TelegramFlow.trendingStickerSetsFlow(): Flow<StickerSets> =
    getUpdatesFlowOfType<TdApi.UpdateTrendingStickerSets>().mapNotNull { it.stickerSets }

/**
 * emits [UpdateRecentStickers] if the list of recently used stickers was updated.
 */
fun TelegramFlow.recentStickersFlow(): Flow<UpdateRecentStickers> = getUpdatesFlowOfType()

/**
 * emits stickerIds [Int[]] if the list of favorite stickers was updated.
 */
fun TelegramFlow.favoriteStickersFlow(): Flow<IntArray> =
    getUpdatesFlowOfType<TdApi.UpdateFavoriteStickers>().mapNotNull { it.stickerIds }