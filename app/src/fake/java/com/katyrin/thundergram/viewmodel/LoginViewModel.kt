package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import javax.inject.Inject

class LoginViewModel @Inject constructor() : BaseViewModel() {

    val errorState: MutableLiveData<String> = MutableLiveData<String>()

    private val mutableAuthState: MutableLiveData<AuthState?> = MutableLiveData()
    val authState: LiveData<AuthState?>
        get() = mutableAuthState

    private val mutableLoggedState: MutableLiveData<Boolean> = MutableLiveData()
    val loggedState: LiveData<Boolean>
        get() = mutableLoggedState

    override fun handleError(error: Throwable) {
        errorState.value = error.message
    }


    fun sendPhone(phone: String) {
        mutableAuthState.value = AuthState.EnterCode
    }

    fun sendCode(code: String) {
        mutableAuthState.value = AuthState.LoggedIn
    }

    fun sendPassword(password: String) {}

    fun setLogged(isLogged: Boolean) {}

    init {
        mutableLoggedState.value = false
        mutableAuthState.value = AuthState.EnterPhone
    }
}