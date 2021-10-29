package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.model.repository.ChatRepository
import com.katyrin.thundergram.viewmodel.appstates.ChatState
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
            mutableLiveData.value = ChatState.Success(chatRepository.getMessages(chatId))
        }
    }
}