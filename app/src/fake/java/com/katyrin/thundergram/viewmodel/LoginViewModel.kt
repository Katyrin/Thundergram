package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import com.katyrin.thundergram.viewmodel.appstates.LoginScreenState
import javax.inject.Inject

class LoginViewModel @Inject constructor() : BaseViewModel() {

    private val mutableLiveData: MutableLiveData<LoginScreenState> = MutableLiveData()
    val liveData: LiveData<LoginScreenState>
        get() = mutableLiveData

    private val mutableAuthState: MutableLiveData<AuthState?> = MutableLiveData()
    val authState: LiveData<AuthState?>
        get() = mutableAuthState

    override fun handleError(error: Throwable) {
        mutableLiveData.value = LoginScreenState.Error(error.message)
    }


    fun sendPhone(phone: String) {
        mutableAuthState.value = AuthState.EnterCode
    }

    fun sendCode(code: String) {
        mutableAuthState.value = AuthState.LoggedIn
    }

    fun resendAuthenticationCode() {
        mutableAuthState.value = AuthState.EnterCode
    }

    fun sendPassword(password: String) {}

    fun setLogged(isLogged: Boolean) {}

    init {
        mutableLiveData.value = LoginScreenState.NotLoggedState
        mutableAuthState.value = AuthState.EnterPhone
    }
}