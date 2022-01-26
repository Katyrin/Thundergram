package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.katyrin.thundergram.model.repository.ChatListRepository
import com.katyrin.thundergram.viewmodel.appstates.ChatListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
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
        if (error.message == PARAMETERS_MESSAGE_ERROR) passParameters()
        else mutableLiveData.value = ChatListState.Error(error.message)
    }

    private fun passParameters() {
        cancelJob()
        viewModelCoroutineScope.launch {
            chatListRepository.getAuthState().collect(::checkRequiredParams)
        }
    }

    private suspend fun checkRequiredParams(state: TdApi.AuthorizationState?): Unit = when (state) {
        is TdApi.AuthorizationStateWaitTdlibParameters -> chatListRepository.setParameters()
        is TdApi.AuthorizationStateWaitEncryptionKey -> chatListRepository.setEncryptionKey()
        else -> getChats()
    }

    fun getChats() {
        cancelJob()
        viewModelCoroutineScope.launch {
            mutableLiveData.value = ChatListState.Success(chatListRepository.getChats())
        }
    }

    fun onVolumeChangeState(chatId: Long, isOn: Boolean): Unit =
        chatListRepository.setIsVolumeOn(chatId, isOn)

    private companion object {
        const val PARAMETERS_MESSAGE_ERROR =
            "Initialization parameters are needed: call setTdlibParameters first"
    }
}