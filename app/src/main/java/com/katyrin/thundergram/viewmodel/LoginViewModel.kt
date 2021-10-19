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

    val errorState: MutableLiveData<String> = MutableLiveData<String>()

    val authState: LiveData<AuthState?> =
        loginRepository.getAuthFlow().flowOn(Dispatchers.Main).asLiveData()

    private val mutableLoggedState: MutableLiveData<Boolean> = MutableLiveData()
    val loggedState: LiveData<Boolean>
        get() = mutableLoggedState

    override fun handleError(error: Throwable) {
        errorState.value = error.message
    }

    fun sendPhone(phone: String) {
        cancelJob()
        viewModelCoroutineScope.launch { loginRepository.sendPhone(phone) }
    }

    fun sendCode(code: String) {
        cancelJob()
        viewModelCoroutineScope.launch { loginRepository.sendCode(code) }
    }

    fun sendPassword(password: String) {
        cancelJob()
        viewModelCoroutineScope.launch { loginRepository.sendPassword(password) }
    }

    fun setLogged(isLogged: Boolean): Unit = loginRepository.setLogged(isLogged)

    init {
        mutableLoggedState.postValue(loginRepository.getLogged())
    }
}