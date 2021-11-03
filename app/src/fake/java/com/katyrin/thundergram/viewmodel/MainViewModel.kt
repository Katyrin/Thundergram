package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.viewmodel.appstates.UserState
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val storage: Storage
) : BaseViewModel() {

    private val mutableLiveData: MutableLiveData<UserState> = MutableLiveData()
    val liveData: LiveData<UserState>
        get() = mutableLiveData

    override fun handleError(error: Throwable) {
        mutableLiveData.value = UserState.Error(error.message)
    }

    private fun checkLogin() {
        cancelJob()
        mutableLiveData.value =
            if (storage.getLogged()) UserState.LoggedState else UserState.NotLoggedState
    }

    init {
        checkLogin()
    }
}