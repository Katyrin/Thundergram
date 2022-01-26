package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.model.repository.ChatRepository
import com.katyrin.thundergram.viewmodel.appstates.ChatState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : BaseViewModel() {

    fun getNewMessage(chatId: Long): LiveData<List<ChatMessage>> =
        chatRepository.getNewMessage(chatId).asLiveData()

    private var mutableLiveData: MutableLiveData<ChatState> = MutableLiveData()
    val liveData: LiveData<ChatState>
        get() = mutableLiveData

    override fun handleError(error: Throwable) {
        mutableLiveData.value = ChatState.Error(error.message)
    }

    fun getMessages(chatId: Long) {
        cancelJob()
        viewModelCoroutineScope.launch {
            var messages: List<ChatMessage> = listOf()
            while (messages.size <= MAX_MESSAGE_SIZE) {
                mutableLiveData.value = ChatState.Success(
                    chatRepository.getHistoryMessages(chatId).also { messages = it }
                )
                delay(PAUSE_BETWEEN_REQUEST)
            }
        }
    }

    fun sendMessage(chatId: Long, message: String) {
        cancelJob()
        viewModelCoroutineScope.launch {
            chatRepository.sendMessage(chatId, message)
        }
    }

    private companion object {
        const val MAX_MESSAGE_SIZE = 100
        const val PAUSE_BETWEEN_REQUEST = 100L
    }
}