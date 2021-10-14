package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.katyrin.thundergram.model.repository.ChatListRepository
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val chatListRepository: ChatListRepository
) : BaseViewModel() {

    private var mutableLiveData: MutableLiveData<List<String>> = MutableLiveData()
    val liveData: LiveData<List<String>>
        get() = mutableLiveData

    override fun handleError(error: Throwable) {}

    fun getChats() {
        cancelJob()
        viewModelCoroutineScope.launch {
            mutableLiveData.value = chatListRepository.getChats().single()
        }
    }
}