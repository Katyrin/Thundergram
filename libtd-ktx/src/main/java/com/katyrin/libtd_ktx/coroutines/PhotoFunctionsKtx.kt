package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.InputFile

/**
 * Suspend function, which deletes a profile photo. If something changes, updateUser will be sent.
 *
 * @param profilePhotoId Identifier of the profile photo to delete.
 */
suspend fun TelegramFlow.deleteProfilePhoto(profilePhotoId: Long): Unit =
    sendFunctionLaunch(TdApi.DeleteProfilePhoto(profilePhotoId))

/**
 * Suspend function, which uploads a new profile photo for the current user. If something changes,
 * updateUser will be sent.
 *
 * @param photo Profile photo to set. inputFileId and inputFileRemote may still be unsupported.
 */
suspend fun TelegramFlow.setProfilePhoto(photo: InputFile?): Unit =
    sendFunctionLaunch(TdApi.SetProfilePhoto(photo))