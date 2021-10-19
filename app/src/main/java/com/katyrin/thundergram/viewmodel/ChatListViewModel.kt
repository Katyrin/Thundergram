package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.katyrin.thundergram.model.repository.ChatListRepository
import com.katyrin.thundergram.viewmodel.appstates.ChatListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val chatListRepository: ChatListRepository
) : BaseViewModel() {

    private var mutableLiveData: MutableLiveData<ChatListState> = MutableLiveData()
    val liveData: LiveData<ChatListState>
        get() = mutableLiveData

    val updateList: LiveData<ChatListState> =
        chatListRepository.updateList().flowOn(Dispatchers.Main).asLiveData()

    override fun handleError(error: Throwable) {
        mutableLiveData.value = ChatListState.Error(error.message)
    }

    fun getChats() {
        cancelJob()
        viewModelCoroutineScope.launch {
            mutableLiveData.value = ChatListState.Success(chatListRepository.getChats())
        }
    }
}