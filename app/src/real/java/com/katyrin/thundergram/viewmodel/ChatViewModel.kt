package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.model.repository.ChatRepository
import com.katyrin.thundergram.model.repository.SoundRepository
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.utils.UNSUBSCRIBE_ID
import com.katyrin.thundergram.viewmodel.appstates.ChatState
import com.katyrin.thundergram.viewmodel.appstates.ChatViewEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val storage: Storage,
    private val soundRepository: SoundRepository
) : BaseViewModel() {

    fun getNewMessage(chatId: Long): LiveData<List<ChatMessage>> =
        chatRepository.getNewMessage(chatId).asLiveData()

    private var mutableLiveData: MutableLiveData<ChatState> = MutableLiveData()
    val liveData: LiveData<ChatState>
        get() = mutableLiveData

    private val _effect = MutableSharedFlow<ChatViewEffect>()
    val effect: SharedFlow<ChatViewEffect> = _effect.asSharedFlow()

    private var cashChatId: Long = UNSUBSCRIBE_ID
    private var cashUserId: Long = UNSUBSCRIBE_ID

    override fun handleError(error: Throwable) {
        if (error.message != UNAUTHORIZED) mutableLiveData.value = ChatState.Error(error.message)
    }

    fun getMessages(chatId: Long) {
        cancelJob()
        viewModelCoroutineScope.launch {
            chatRepository.openChat(chatId)
            var messages: List<ChatMessage> = listOf()
            while (messages.size <= MAX_MESSAGE_SIZE) {
                val messageList = chatRepository.getHistoryMessages(chatId).also { messages = it }
                mutableLiveData.value = ChatState.Success(messageList)
                delay(PAUSE_BETWEEN_REQUEST)
            }
        }
    }

    fun sendMessage(chatId: Long, message: String) {
        cancelJob()
        viewModelCoroutineScope.launch { chatRepository.sendMessage(chatId, message) }
    }

    fun checkExistSubscribe(chatId: Long, userId: Long): Unit = with(storage) {
        val isSubscribed: Boolean = chatId == getSubscribeChatId() && userId == getSubscribeUserId()
        if (isSubscribed) {
            cashChatId = UNSUBSCRIBE_ID
            cashUserId = UNSUBSCRIBE_ID
        } else {
            cashChatId = chatId
            cashUserId = userId
        }
        val chatViewEffect: ChatViewEffect =
            if (isSubscribed) ChatViewEffect.OnIsSubscribed else ChatViewEffect.OnIsNotSubscribed
        viewModelScope.launch { _effect.emit(chatViewEffect) }
    }

    fun onClickUserMenu() {
        storage.setSubscribeChatId(cashChatId)
        storage.setSubscribeUserId(cashUserId)
    }

    fun onUpdateSoundSpeed(): Unit = soundRepository.onUpdateSoundSpeed()

    fun onClickPlayButton(voice: ChatMessage.Voice, isPlayState: Boolean) {
        viewModelCoroutineScope.launch { soundRepository.onClickPlayButton(voice, isPlayState) }
    }

    fun onUpdateSeekTo(positionMs: Long): Unit = soundRepository.onUpdateSeekTo(positionMs)

    private companion object {
        const val MAX_MESSAGE_SIZE = 100
        const val PAUSE_BETWEEN_REQUEST = 100L
        const val UNAUTHORIZED = "Unauthorized"
    }
}