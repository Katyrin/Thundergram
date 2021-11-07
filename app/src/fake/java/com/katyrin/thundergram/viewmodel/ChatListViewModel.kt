package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.katyrin.thundergram.model.entities.ChatListItem
import com.katyrin.thundergram.viewmodel.appstates.ChatListState
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListViewModel @Inject constructor() : BaseViewModel() {

    private var mutableLiveData: MutableLiveData<ChatListState> = MutableLiveData()
    val liveData: LiveData<ChatListState>
        get() = mutableLiveData

    val updateList: LiveData<ChatListState> = MutableLiveData()

    override fun handleError(error: Throwable) {
        mutableLiveData.value = ChatListState.Error(error.message)
    }

    fun getChats() {
        cancelJob()
        viewModelCoroutineScope.launch {
            mutableLiveData.value = ChatListState.Success(getChatList())
        }
    }

    private fun getChatList(): List<ChatListItem> = mutableListOf<ChatListItem>().apply {
        for (i in 1..100) add(ChatListItem(i.toLong(), "$CHAT_NAME$i", ""))
    }

    private companion object {
        const val CHAT_NAME = "Chat name: "
    }
}