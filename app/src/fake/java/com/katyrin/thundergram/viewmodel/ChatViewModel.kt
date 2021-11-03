package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.viewmodel.appstates.ChatState
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor() : BaseViewModel() {

    fun getNewMessage(chatId: Long): LiveData<List<ChatMessage>> = MutableLiveData()

    private var mutableLiveData: MutableLiveData<ChatState> = MutableLiveData()
    val liveData: LiveData<ChatState>
        get() = mutableLiveData

    override fun handleError(error: Throwable) {
        mutableLiveData.value = ChatState.Error(error.message)
    }

    fun getMessages(chatId: Long) {
        cancelJob()
        viewModelCoroutineScope.launch {
            mutableLiveData.value = ChatState.Success(getChatMessages())
        }
    }

    private fun getChatMessages(): List<ChatMessage> = mutableListOf<ChatMessage>().apply {
        for (i in 1..100) add(
            ChatMessage.Text(i.toLong(), "$TEXT_MESSAGE$i", "", i % 2 == 0)
        )
    }

    fun sendMessage(chatId: Long, message: String) {
        cancelJob()
        viewModelCoroutineScope.launch {

        }
    }

    private companion object {
        const val TEXT_MESSAGE = "This is simple message number "
    }
}