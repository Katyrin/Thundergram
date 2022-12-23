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
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.utils.PARAMETERS_MESSAGE_ERROR
import dagger.android.AndroidInjection
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
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

    @Inject
    lateinit var storage: Storage

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
    }

    private suspend fun getNewMessage() {
        storage.getVolumeOnChats().forEach { mainRepository.openChat(it) }
        mainRepository.getNewMessageFlow()
            .flowOn(Dispatchers.Default)
            .filter(::filterDoubleMessage)
            .collect(::renderNewMessage)
    }

    private fun filterDoubleMessage(chatMessage: ChatMessage): Boolean = when (chatMessage) {
        is ChatMessage.Text ->
            if (storage.isVolumeOn(chatMessage.chatId) && chatMessage.userId != storage.getMyUserId())
                if (messageId != chatMessage.messageId) true else chatId != chatMessage.chatId
            else false
        is ChatMessage.Photo ->
            if (storage.isVolumeOn(chatMessage.chatId) && chatMessage.userId != storage.getMyUserId())
                if (messageId != chatMessage.messageId) true else chatId != chatMessage.chatId
            else false
        is ChatMessage.Voice ->
            if (storage.isVolumeOn(chatMessage.chatId) && chatMessage.userId != storage.getMyUserId())
                if (messageId != chatMessage.messageId) true else chatId != chatMessage.chatId
            else false
    }

    private fun renderNewMessage(chatMessage: ChatMessage): Unit = when (chatMessage) {
        is ChatMessage.Text -> renderTextMessage(chatMessage)
        is ChatMessage.Photo -> renderPhotoMessage(chatMessage)
        is ChatMessage.Voice -> renderVoiceMessage(chatMessage)
    }

    private fun renderTextMessage(chatMessage: ChatMessage.Text) {
        chatId = chatMessage.chatId
        messageId = chatMessage.messageId
        notificationGenerator.showMessageNotification(
            chatMessage.userId,
            chatMessage.chatId,
            chatMessage.chatName,
            chatMessage.userName,
            chatMessage.message,
            chatMessage.userPhotoPath
        )
    }

    private fun renderPhotoMessage(chatMessage: ChatMessage.Photo) {
        chatId = chatMessage.chatId
        messageId = chatMessage.messageId
        notificationGenerator.showMessageNotification(
            chatMessage.userId,
            chatMessage.chatId,
            chatMessage.chatName,
            chatMessage.userName,
            getString(R.string.photo_message),
            chatMessage.userPhotoPath
        )
    }

    private fun renderVoiceMessage(chatMessage: ChatMessage.Voice) {
        chatId = chatMessage.chatId
        messageId = chatMessage.messageId
        notificationGenerator.showMessageNotification(
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
        serviceScope.launch {
            mainRepository.updateUserId()
            getNewMessage()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        cancelJob()
        super.onDestroy()
    }

    private companion object {
        const val NOTIFICATION_ID = 154
        var messageId: Long = 0
        var chatId: Long = 0
    }
}