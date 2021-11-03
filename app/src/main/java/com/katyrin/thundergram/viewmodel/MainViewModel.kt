package com.katyrin.thundergram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.checkDatabaseEncryptionKey
import com.katyrin.libtd_ktx.coroutines.setTdlibParameters
import com.katyrin.libtd_ktx.extensions.UserKtx
import com.katyrin.libtd_ktx.flows.authorizationStateFlow
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.viewmodel.appstates.UserState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val parameters: TdApi.TdlibParameters,
    private val storage: Storage,
    override val api: TelegramFlow,
) : BaseViewModel(), UserKtx {

    private val mutableLiveData: MutableLiveData<UserState> = MutableLiveData()
    val liveData: LiveData<UserState>
        get() = mutableLiveData

    override fun handleError(error: Throwable) {
        mutableLiveData.value = UserState.Error(error.message)
    }

    private fun passParameters() {
        cancelJob()
        viewModelCoroutineScope.launch {
            api.authorizationStateFlow().collect(::checkRequiredParams)
        }
    }

    private suspend fun checkRequiredParams(state: TdApi.AuthorizationState?): Unit =
        when (state) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> api.setTdlibParameters(parameters)
            is TdApi.AuthorizationStateWaitEncryptionKey -> api.checkDatabaseEncryptionKey()
            else -> checkLogin()
        }

    private fun checkLogin() {
        cancelJob()
        mutableLiveData.value =
            if (storage.getLogged()) UserState.LoggedState else UserState.NotLoggedState
    }

    init {
        passParameters()
    }
}