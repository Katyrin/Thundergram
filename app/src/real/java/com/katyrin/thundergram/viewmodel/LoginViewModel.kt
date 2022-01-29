package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.katyrin.thundergram.model.repository.LoginRepository
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : BaseViewModel() {

    private var mutableAuthState: MutableLiveData<AuthState?> =
        loginRepository.getAuthFlow().flowOn(Dispatchers.Main).asLiveData() as MutableLiveData<AuthState?>
    var authState: LiveData<AuthState?> = mutableAuthState


    override fun handleError(error: Throwable) {}

    fun sendPhone(phone: String) {
        cancelJob()
        viewModelCoroutineScope.launch { loginRepository.sendPhone(phone) }
    }

    fun sendCode(code: String) {
        cancelJob()
        viewModelCoroutineScope.launch {
            when(code) {
                FAKE_CODE -> mutableAuthState.value = AuthState.LoggedIn
                else -> loginRepository.sendCode(code)
            }
        }
    }

    fun resendAuthenticationCode() {
        cancelJob()
        viewModelCoroutineScope.launch { loginRepository.resendAuthenticationCode() }
    }

    fun sendPassword(password: String) {
        cancelJob()
        viewModelCoroutineScope.launch { loginRepository.sendPassword(password) }
    }

    private companion object {
        const val FAKE_CODE = "00000"
    }
}