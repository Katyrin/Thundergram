package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.viewmodel.appstates.UserMenuState
import javax.inject.Inject

class UserMenuViewModel @Inject constructor(
    private val storage: Storage
) : ViewModel() {

    private var mutableLiveData: MutableLiveData<UserMenuState> = MutableLiveData()
    val liveData: LiveData<UserMenuState>
        get() = mutableLiveData

    fun checkExistSubscribe(chatId: Long, userId: Long): Unit = with(storage) {
        val isExistSubscribe: Boolean =
            chatId == getSubscribeChatId() && userId == getSubscribeUserId()
        mutableLiveData.value =
            if (isExistSubscribe) UserMenuState.ShowUnsubscribe
            else UserMenuState.ShowSubscribe(chatId, userId)
    }

    fun subscribeUserInChat(chatId: Long, userId: Long) {
        storage.setSubscribeChatId(chatId)
        storage.setSubscribeUserId(userId)
    }
}