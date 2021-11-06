package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.katyrin.thundergram.model.repository.MainRepository
import com.katyrin.thundergram.viewmodel.appstates.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    private val mutableLiveData: MutableLiveData<UserState> = MutableLiveData()
    val liveData: LiveData<UserState>
        get() = mutableLiveData

    val callSubscribedPhone: LiveData<String> =
        mainRepository.getSubscribedPhone().flowOn(Dispatchers.Main).asLiveData()

    override fun handleError(error: Throwable) {
        mutableLiveData.value = UserState.Error(error.message)
    }

    private fun checkLogin() {
        cancelJob()
        viewModelCoroutineScope.launch {
            mutableLiveData.value = mainRepository.getUserState()
        }
    }

    init {
        checkLogin()
    }
}