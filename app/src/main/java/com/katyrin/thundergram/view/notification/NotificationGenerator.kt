package com.katyrin.thundergram.view.notification

import android.app.Notification

interface NotificationGenerator {
    fun getForegroundNotification(): Notification
    fun showMessageNotification(
        userId: Long,
        chatId: Long,
        chatName: String,
        title: String,
        message: String,
        userPhotoPath: String?
    )
}