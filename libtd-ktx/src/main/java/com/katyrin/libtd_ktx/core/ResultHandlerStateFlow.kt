package com.katyrin.libtd_ktx.core

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import org.drinkless.td.libcore.telegram.TdApi

/**
 * Class that converts results handler from [TdApi] client to [Flow]
 * of the [TdApi.Object] using [MutableStateFlow]
 */
class ResultHandlerStateFlow(
    private val stateFlow: MutableStateFlow<TdApi.Object?> = MutableStateFlow(null)
) : TelegramFlow.ResultHandlerFlow, Flow<TdApi.Object> by stateFlow.filterNotNull() {

    override fun onResult(result: TdApi.Object?) {
        Log.i("TdApi.Object", "TdApi.Object $result")
        when (result) {
            is TdApi.UpdateNewMessage -> stateFlow.value = result
            is TdApi.UpdateAuthorizationState -> stateFlow.value = result
        }
    }
}