package com.katyrin.thundergram.view.notification

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import com.katyrin.thundergram.R
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.model.repository.ChatListRepository
import com.katyrin.thundergram.model.repository.MainRepository
import com.katyrin.thundergram.utils.PARAMETERS_MESSAGE_ERROR
import dagger.android.AndroidInjection
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class NotificationService : Service() {

    @Inject
    lateinit var mainRepository: MainRepository

    @Inject
    lateinit var chatListRepository: ChatListRepository

    @Inject
    lateinit var notificationGenerator: NotificationGenerator

    private val serviceScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) }
    )

    private fun handleError(error: Throwable) {
        if (error.message == PARAMETERS_MESSAGE_ERROR) passParameters()
    }

    private fun passParameters() {
        cancelJob()
        serviceScope.launch { chatListRepository.getAuthState().collect(::checkRequiredParams) }
    }

    private suspend fun checkRequiredParams(state: TdApi.AuthorizationState?): Unit = when (state) {
        is TdApi.AuthorizationStateWaitTdlibParameters -> chatListRepository.setParameters()
        is TdApi.AuthorizationStateWaitEncryptionKey -> chatListRepository.setEncryptionKey()
        else -> getNewMessage()
    }

    private fun cancelJob() {
        serviceScope.coroutineContext.cancelChildren()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        serviceScope.launch {
            mainRepository.updateUserId()
            getNewMessage()
        }
    }

    private suspend fun getNewMessage() {
        mainRepository.getNewMessageFlow()
            .flowOn(Dispatchers.Default)
            .collect(::renderNewMessage)
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