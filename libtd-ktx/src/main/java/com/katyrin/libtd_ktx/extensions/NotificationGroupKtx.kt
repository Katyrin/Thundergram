package com.katyrin.libtd_ktx.extensions

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.removeNotification
import com.katyrin.libtd_ktx.coroutines.removeNotificationGroup
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.NotificationGroup

/**
 * Interface for access [TdApi.NotificationGroup] extension functions. Can be used alongside with
 * other extension interfaces of the package. Must contain [TelegramFlow] instance field to access its
 * functionality
 */
interface NotificationGroupKtx : BaseKtx {
    /**
     * Instance of the [TelegramFlow] connecting extensions to the Telegram Client
     */
    override val api: TelegramFlow

    /**
     * Suspend function, which removes an active notification from notification list. Needs to be
     * called only if the notification is removed by the current user.
     *
     * @param notificationId Identifier of removed notification.
     */
    suspend fun NotificationGroup.removeNotification(notificationId: Int): Unit =
        api.removeNotification(id, notificationId)

    /**
     * Suspend function, which removes a group of active notifications. Needs to be called only if the
     * notification group is removed by the current user.
     *
     * @param maxNotificationId The maximum identifier of removed notifications.
     */
    suspend fun NotificationGroup.remove(maxNotificationId: Int): Unit =
        api.removeNotificationGroup(id, maxNotificationId)
}