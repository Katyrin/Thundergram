package com.katyrin.thundergram.view.notification

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import com.katyrin.thundergram.R
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.model.repository.MainRepository
import dagger.android.AndroidInjection
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NotificationService : Service() {

    @Inject
    lateinit var mainRepository: MainRepository

    @Inject
    lateinit var notificationGenerator: NotificationGenerator

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private fun cancelJob() {
        serviceScope.coroutineContext.cancelChildren()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        serviceScope.launch {
            mainRepository.getNewMessageFlow()
                .flowOn(Dispatchers.Default)
                .collect(::renderNewMessage)
        }
    }

    private fun renderNewMessage(chatMessage: ChatMessage): Unit = when (chatMessage) {
        is ChatMessage.Text -> notificationGenerator.showMessageNotification(
            chatMessage.userId,
            chatMessage.chatId,
            chatMessage.chatName,
            chatMessage.userName,
            chatMessage.message,
            chatMessage.userPhotoPath
        )
        is ChatMessage.Photo -> notificationGenerator.showMessageNotification(
            chatMessage.userId,
            chatMessage.chatId,
            chatMessage.chatName,
            chatMessage.userName,
            getString(R.string.photo_message),
            chatMessage.userPhotoPath
        )
        is ChatMessage.Voice -> notificationGenerator.showMessageNotification(
            chatMessage.userId,
            chatMessage.chatId,
            chatMessage.chatName,
            chatMessage.userName,
            getString(R.string.voice_message),
            chatMessage.userPhotoPath
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) startForeground(
            NOTIFICATION_ID,
            notificationGenerator.getForegroundNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL
        )
        else startForeground(NOTIFICATION_ID, notificationGenerator.getForegroundNotification())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        cancelJob()
        super.onDestroy()
    }

    private companion object {
        const val NOTIFICATION_ID = 154
    }
}