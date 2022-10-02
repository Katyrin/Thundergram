package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.katyrin.thundergram.model.generator.ChatListGenerator
import com.katyrin.thundergram.model.repository.ChatListRepository
import com.katyrin.thundergram.utils.PARAMETERS_MESSAGE_ERROR
import com.katyrin.thundergram.viewmodel.appstates.ChatListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val chatListRepository: ChatListRepository,
    private val chatListGenerator: ChatListGenerator
) : BaseViewModel() {

    private var mutableLiveData: MutableLiveData<ChatListState> = MutableLiveData()
    val liveData: LiveData<ChatListState>
        get() = mutableLiveData

    val updateList: LiveData<ChatListState> =
        chatListRepository.updateList().flowOn(Dispatchers.Main).asLiveData()

    override fun handleError(error: Throwable) {
        when (error.message) {
            PARAMETERS_MESSAGE_ERROR -> passParameters()
            UNAUTHORIZED -> mutableLiveData.value =
                ChatListState.Success(chatListGenerator.generateChatList())
            else -> setErrorState(error.message)
        }
    }

    private fun setErrorState(message: String?) {
        mutableLiveData.value = ChatListState.Error(message)
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
        is TdApi.AuthorizationStateReady -> getChats()
        else -> setErrorState(SOMETHING_WRONG)
    }

    fun getChats() {
        cancelJob()
        mutableLiveData.value = ChatListState.LoadState
        viewModelCoroutineScope.launch {
            mutableLiveData.value = ChatListState.Success(chatListRepository.getChats(), true)
        }
    }

    fun onVolumeChangeState(chatId: Long, isOn: Boolean): Unit =
        chatListRepository.setIsVolumeOn(chatId, isOn)

    private companion object {
        const val UNAUTHORIZED = "Unauthorized"
        const val SOMETHING_WRONG = "Something wrong"
    }
}