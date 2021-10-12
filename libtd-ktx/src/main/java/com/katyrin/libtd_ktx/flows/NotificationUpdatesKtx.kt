package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlin.Array
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.NotificationGroup
import org.drinkless.td.libcore.telegram.TdApi.UpdateHavePendingNotifications
import org.drinkless.td.libcore.telegram.TdApi.UpdateNotification
import org.drinkless.td.libcore.telegram.TdApi.UpdateNotificationGroup
import org.drinkless.td.libcore.telegram.TdApi.UpdateScopeNotificationSettings
import org.drinkless.td.libcore.telegram.TdApi.UpdateServiceNotification

/**
 * emits [UpdateScopeNotificationSettings] if notification settings for some type of chats were
 * updated.
 */
fun TelegramFlow.scopeNotificationSettingsFlow(): Flow<UpdateScopeNotificationSettings> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateNotification] if a notification was changed.
 */
fun TelegramFlow.notificationFlow(): Flow<UpdateNotification> = getUpdatesFlowOfType()

/**
 * emits [UpdateNotificationGroup] if a list of active notifications in a notification group has
 * changed.
 */
fun TelegramFlow.notificationGroupFlow(): Flow<UpdateNotificationGroup> = getUpdatesFlowOfType()

/**
 * emits groups [NotificationGroup[]] if contains active notifications that was shown on previous
 * application launches. This update is sent only if the message database is used. In that case it
 * comes once before any updateNotification and updateNotificationGroup update.
 */
fun TelegramFlow.activeNotificationsFlow(): Flow<Array<NotificationGroup>> =
    getUpdatesFlowOfType<TdApi.UpdateActiveNotifications>().mapNotNull { it.groups }

/**
 * emits [UpdateHavePendingNotifications] if describes whether there are some pending notification
 * updates. Can be used to prevent application from killing, while there are some pending
 * notifications.
 */
fun TelegramFlow.havePendingNotificationsFlow(): Flow<UpdateHavePendingNotifications> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateServiceNotification] if service notification from the server. Upon receiving this
 * the client must show a popup with the content of the notification.
 */
fun TelegramFlow.serviceNotificationFlow(): Flow<UpdateServiceNotification> = getUpdatesFlowOfType()