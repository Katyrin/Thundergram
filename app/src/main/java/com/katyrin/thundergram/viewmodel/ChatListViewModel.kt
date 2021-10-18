package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.katyrin.thundergram.model.entities.ChatListItem
import com.katyrin.thundergram.model.repository.ChatListRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val chatListRepository: ChatListRepository
) : BaseViewModel() {

    private var mutableLiveData: MutableLiveData<List<ChatListItem>> = MutableLiveData()
    val liveData: LiveData<List<ChatListItem>>
        get() = mutableLiveData

    val updateList: LiveData<List<ChatListItem>> = chatListRepository.updateList().asLiveData()

    override fun handleError(error: Throwable) {}

    fun getChats() {
        cancelJob()
        viewModelCoroutineScope.launch {
            mutableLiveData.value = chatListRepository.getChats()
        }
    }
}