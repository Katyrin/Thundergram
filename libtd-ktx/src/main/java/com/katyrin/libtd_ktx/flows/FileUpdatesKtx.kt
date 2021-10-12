package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlin.Long
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.File
import org.drinkless.td.libcore.telegram.TdApi.UpdateFileGenerationStart

/**
 * emits [File] if information about a file was updated.
 */
fun TelegramFlow.fileFlow(): Flow<File> =
    getUpdatesFlowOfType<TdApi.UpdateFile>().mapNotNull { it.file }

/**
 * emits [UpdateFileGenerationStart] if the file generation process needs to be started by the
 * client.
 */
fun TelegramFlow.fileGenerationStartFlow(): Flow<UpdateFileGenerationStart> = getUpdatesFlowOfType()

/**
 * emits generationId [Long] if file generation is no longer needed.
 */
fun TelegramFlow.fileGenerationStopFlow(): Flow<Long> =
    getUpdatesFlowOfType<TdApi.UpdateFileGenerationStop>().mapNotNull { it.generationId }