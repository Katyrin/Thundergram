package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.katyrin.thundergram.model.repository.LoginRepository
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import com.katyrin.thundergram.viewmodel.appstates.LoginScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : BaseViewModel() {

    private val mutableLiveData: MutableLiveData<LoginScreenState> = MutableLiveData()
    val liveData: LiveData<LoginScreenState>
        get() = mutableLiveData

    val authState: LiveData<AuthState?> =
        loginRepository.getAuthFlow().flowOn(Dispatchers.Main).asLiveData()

    override fun handleError(error: Throwable) {
        mutableLiveData.value = LoginScreenState.Error(error.message)
    }

    fun sendPhone(phone: String) {
        cancelJob()
        viewModelCoroutineScope.launch { loginRepository.sendPhone(phone) }
    }

    fun sendCode(code: String) {
        cancelJob()
        viewModelCoroutineScope.launch { loginRepository.sendCode(code) }
    }

    fun resendAuthenticationCode() {
        cancelJob()
        viewModelCoroutineScope.launch { loginRepository.resendAuthenticationCode() }
    }

    fun sendPassword(password: String) {
        cancelJob()
        viewModelCoroutineScope.launch { loginRepository.sendPassword(password) }
    }

    fun setLogged(isLogged: Boolean): Unit = loginRepository.setLogged(isLogged)

    init {
        mutableLiveData.postValue(
            if (loginRepository.getLogged()) LoginScreenState.LoggedState
            else LoginScreenState.NotLoggedState
        )
    }
}