package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlin.Array
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.ChatNearby
import org.drinkless.td.libcore.telegram.TdApi.UpdateUserFullInfo
import org.drinkless.td.libcore.telegram.TdApi.UpdateUserPrivacySettingRules
import org.drinkless.td.libcore.telegram.TdApi.UpdateUserStatus
import org.drinkless.td.libcore.telegram.TdApi.User

/**
 * emits [UpdateUserStatus] if the user went online or offline.
 */
fun TelegramFlow.userStatusFlow(): Flow<UpdateUserStatus> = getUpdatesFlowOfType()

/**
 * emits [User] if some data of a user has changed. This update is guaranteed to come before the
 * user identifier is returned to the client.
 */
fun TelegramFlow.userFlow(): Flow<User> =
    getUpdatesFlowOfType<TdApi.UpdateUser>().mapNotNull { it.user }

/**
 * emits [UpdateUserFullInfo] if some data from userFullInfo has been changed.
 */
fun TelegramFlow.userFullInfoFlow(): Flow<UpdateUserFullInfo> = getUpdatesFlowOfType()

/**
 * emits [UpdateUserPrivacySettingRules] if some privacy setting rules have been changed.
 */
fun TelegramFlow.userPrivacySettingRulesFlow(): Flow<UpdateUserPrivacySettingRules> =
    getUpdatesFlowOfType()

/**
 * emits usersNearby [ChatNearby[]] if list of users nearby has changed. The update is sent only 60
 * seconds after a successful searchChatsNearby request.
 */
fun TelegramFlow.usersNearbyFlow(): Flow<Array<ChatNearby>> =
    getUpdatesFlowOfType<TdApi.UpdateUsersNearby>().mapNotNull { it.usersNearby }