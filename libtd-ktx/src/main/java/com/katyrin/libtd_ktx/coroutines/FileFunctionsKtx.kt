package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which stops the downloading of a file. If a file has already been downloaded,
 * does nothing.
 *
 * @param fileId Identifier of a file to stop downloading.
 * @param onlyIfPending Pass true to stop downloading only if it hasn't been started, i.e. request
 * hasn't been sent to server.
 */
suspend fun TelegramFlow.cancelDownloadFile(fileId: Int, onlyIfPending: Boolean): Unit =
    sendFunctionLaunch(CancelDownloadFile(fileId, onlyIfPending))

/**
 * Suspend function, which stops the uploading of a file. Supported only for files uploaded by using
 * uploadFile. For other files the behavior is undefined.
 *
 * @param fileId Identifier of the file to stop uploading.
 */
suspend fun TelegramFlow.cancelUploadFile(fileId: Int): Unit =
    sendFunctionLaunch(CancelUploadFile(fileId))

/**
 * Suspend function, which removes potentially dangerous characters from the name of a file. The
 * encoding of the file name is supposed to be UTF-8. Returns an empty string on failure. This is an
 * offline method. Can be called before authorization. Can be called synchronously.
 *
 * @param fileName File name or path to the file.
 *
 * @return [Text] Contains some text.
 */
suspend fun TelegramFlow.cleanFileName(fileName: String?): Text =
    sendFunctionAsync(CleanFileName(fileName))

/**
 * Suspend function, which deletes a file from the TDLib file cache.
 *
 * @param fileId Identifier of the file to delete.
 */
suspend fun TelegramFlow.deleteFile(fileId: Int): Unit = sendFunctionLaunch(DeleteFile(fileId))

/**
 * Suspend function, which downloads a file from the cloud. Download progress and completion of the
 * download will be notified through updateFile updates.
 *
 * @param fileId Identifier of the file to download.
 * @param priority Priority of the download (1-32). The higher the priority, the earlier the file
 * will be downloaded. If the priorities of two files are equal, then the last one for which
 * downloadFile was called will be downloaded first.
 * @param offset The starting position from which the file should be downloaded.
 * @param limit Number of bytes which should be downloaded starting from the &quot;offset&quot;
 * position before the download will be automatically cancelled; use 0 to download without a limit.
 * @param synchronous If false, this request returns file state just after the download has been
 * started. If true, this request returns file state only after the download has succeeded, has failed,
 * has been cancelled or a new downloadFile request with different offset/limit parameters was sent.
 *
 * @return [File] Represents a file.
 */
suspend fun TelegramFlow.downloadFile(
    fileId: Int,
    priority: Int,
    offset: Int,
    limit: Int,
    synchronous: Boolean
): File = sendFunctionAsync(DownloadFile(fileId, priority, offset, limit, synchronous))

/**
 * Suspend function, which finishes the file generation.
 *
 * @param generationId The identifier of the generation process.
 * @param error If set, means that file generation has failed and should be terminated.
 */
suspend fun TelegramFlow.finishFileGeneration(generationId: Long, error: Error?): Unit =
    sendFunctionLaunch(FinishFileGeneration(generationId, error))

/**
 * Suspend function, which returns information about a file; this is an offline request.
 *
 * @param fileId Identifier of the file to get.
 *
 * @return [File] Represents a file.
 */
suspend fun TelegramFlow.getFile(fileId: Int): File = sendFunctionAsync(GetFile(fileId))

/**
 * Suspend function, which returns file downloaded prefix size from a given offset.
 *
 * @param fileId Identifier of the file.
 * @param offset Offset from which downloaded prefix size should be calculated.
 *
 * @return [Count] Contains a counter.
 */
suspend fun TelegramFlow.getFileDownloadedPrefixSize(fileId: Int, offset: Int): Count =
    sendFunctionAsync(GetFileDownloadedPrefixSize(fileId, offset))

/**
 * Suspend function, which returns the extension of a file, guessed by its MIME type. Returns an
 * empty string on failure. This is an offline method. Can be called before authorization. Can be
 * called synchronously.
 *
 * @param mimeType The MIME type of the file.
 *
 * @return [Text] Contains some text.
 */
suspend fun TelegramFlow.getFileExtension(mimeType: String?): Text =
    sendFunctionAsync(GetFileExtension(mimeType))

/**
 * Suspend function, which returns the MIME type of a file, guessed by its extension. Returns an
 * empty string on failure. This is an offline method. Can be called before authorization. Can be
 * called synchronously.
 *
 * @param fileName The name of the file or path to the file.
 *
 * @return [Text] Contains some text.
 */
suspend fun TelegramFlow.getFileMimeType(fileName: String?): Text =
    sendFunctionAsync(GetFileMimeType(fileName))

/**
 * Suspend function, which returns information about a file with a map thumbnail in PNG format. Only
 * map thumbnail files with size less than 1MB can be downloaded.
 *
 * @param location Location of the map center.
 * @param zoom Map zoom level; 13-20.
 * @param width Map width in pixels before applying scale; 16-1024.
 * @param height Map height in pixels before applying scale; 16-1024.
 * @param scale Map scale; 1-3.
 * @param chatId Identifier of a chat, in which the thumbnail will be shown. Use 0 if unknown.
 *
 * @return [File] Represents a file.
 */
suspend fun TelegramFlow.getMapThumbnailFile(
    location: Location?,
    zoom: Int,
    width: Int,
    height: Int,
    scale: Int,
    chatId: Long
): File = sendFunctionAsync(GetMapThumbnailFile(location, zoom, width, height, scale, chatId))

/**
 * Suspend function, which returns information about a file by its remote ID; this is an offline
 * request. Can be used to register a URL as a file for further uploading, or sending as a message.
 * Even the request succeeds, the file can be used only if it is still accessible to the user. For
 * example, if the file is from a message, then the message must be not deleted and accessible to the
 * user. If the file database is disabled, then the corresponding object with the file must be
 * preloaded by the client.
 *
 * @param remoteFileId Remote identifier of the file to get.
 * @param fileType File type, if known.
 *
 * @return [File] Represents a file.
 */
suspend fun TelegramFlow.getRemoteFile(remoteFileId: String?, fileType: FileType?): File =
    sendFunctionAsync(GetRemoteFile(remoteFileId, fileType))

/**
 * Suspend function, which reads a part of a file from the TDLib file cache and returns read bytes.
 * This method is intended to be used only if the client has no direct access to TDLib's file system,
 * because it is usually slower than a direct read from the file.
 *
 * @param fileId Identifier of the file. The file must be located in the TDLib file cache.
 * @param offset The offset from which to read the file.
 * @param count Number of bytes to read. An error will be returned if there are not enough bytes
 * available in the file from the specified position. Pass 0 to read all available data from the
 * specified position.
 *
 * @return [FilePart] Contains a part of a file.
 */
suspend fun TelegramFlow.readFilePart(
    fileId: Int,
    offset: Int,
    count: Int
): FilePart = sendFunctionAsync(ReadFilePart(fileId, offset, count))

/**
 * Suspend function, which informs TDLib on a file generation progress.
 *
 * @param generationId The identifier of the generation process.
 * @param expectedSize Expected size of the generated file, in bytes; 0 if unknown.
 * @param localPrefixSize The number of bytes already generated.
 */
suspend fun TelegramFlow.setFileGenerationProgress(
    generationId: Long,
    expectedSize: Int,
    localPrefixSize: Int
): Unit = sendFunctionLaunch(SetFileGenerationProgress(generationId, expectedSize, localPrefixSize))

/**
 * Suspend function, which asynchronously uploads a file to the cloud without sending it in a
 * message. updateFile will be used to notify about upload progress and successful completion of the
 * upload. The file will not have a persistent remote identifier until it will be sent in a message.
 *
 * @param file File to upload.
 * @param fileType File type.
 * @param priority Priority of the upload (1-32). The higher the priority, the earlier the file will
 * be uploaded. If the priorities of two files are equal, then the first one for which uploadFile was
 * called will be uploaded first.
 *
 * @return [File] Represents a file.
 */
suspend fun TelegramFlow.uploadFile(
    file: InputFile?,
    fileType: FileType?,
    priority: Int
): File = sendFunctionAsync(UploadFile(file, fileType, priority))

/**
 * Suspend function, which uploads a PNG image with a sticker; for bots only; returns the uploaded
 * file.
 *
 * @param userId Sticker file owner.
 * @param pngSticker PNG image with the sticker; must be up to 512 kB in size and fit in 512x512
 * square.
 *
 * @return [File] Represents a file.
 */
suspend fun TelegramFlow.uploadStickerFile(userId: Int, pngSticker: InputFile?): File =
    sendFunctionAsync(UploadStickerFile(userId, pngSticker))

/**
 * Suspend function, which writes a part of a generated file. This method is intended to be used
 * only if the client has no direct access to TDLib's file system, because it is usually slower than a
 * direct write to the destination file.
 *
 * @param generationId The identifier of the generation process.
 * @param offset The offset from which to write the data to the file.
 * @param data The data to write.
 */
suspend fun TelegramFlow.writeGeneratedFilePart(
    generationId: Long,
    offset: Int,
    data: ByteArray?
): Unit = sendFunctionLaunch(WriteGeneratedFilePart(generationId, offset, data))