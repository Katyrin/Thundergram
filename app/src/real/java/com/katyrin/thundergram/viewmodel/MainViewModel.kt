package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.katyrin.thundergram.model.entities.FirebaseEventResponse
import com.katyrin.thundergram.model.repository.MainRepository
import com.katyrin.thundergram.viewmodel.appstates.UserState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    private val mutableLiveData: MutableLiveData<UserState> = MutableLiveData()
    val liveData: LiveData<UserState>
        get() = mutableLiveData

    override fun handleError(error: Throwable) {
        mutableLiveData.value = UserState.Error(error.message)
    }

    fun callSubscribedPhone() {
        viewModelCoroutineScope.launch {
            mainRepository.getSubscribedPhone().collect { phoneNumber ->
                mutableLiveData.value = UserState.CallState(phoneNumber, THREE_COINS)
            }
        }
    }

    fun checkLogin() {
        cancelJob()
        viewModelCoroutineScope.launch {
            mutableLiveData.value = mainRepository.getUserState()
        }
    }

    fun saveCoins(coins: Long) {
        viewModelCoroutineScope.launch {
            mainRepository.setCoins(coins)
        }
    }

    fun updateCoins() {
        viewModelCoroutineScope.launch {
            setLivedataValueByResult(mainRepository.getSingleFirebaseEventResponse())
        }
    }

    fun getUpdatesCoins() {
        viewModelCoroutineScope.launch {
            mainRepository.getUpdateCoinsFlow().collect(::setLivedataValueByResult)
        }
    }

    private suspend fun setLivedataValueByResult(result: FirebaseEventResponse) {
        mutableLiveData.value = when (result) {
            is FirebaseEventResponse.Success ->
                UserState.UpdateCoinState(getCurrentCoins(result.snapshot))
            is FirebaseEventResponse.Cancelled -> UserState.Error(result.error.message)
        }
    }

    private suspend fun getCurrentCoins(snapshot: DataSnapshot): Int {
        val currentCoins: Long = snapshot.value as Long? ?: DEFAULT_COINS
        if (currentCoins == DEFAULT_COINS) mainRepository.setCoins(DEFAULT_COINS)
        return currentCoins.toInt()
    }

    private companion object {
        const val DEFAULT_COINS = 10L
        const val THREE_COINS = 3
    }
}