package com.katyrin.thundergram.billing

import android.app.Activity
import kotlinx.coroutines.flow.Flow

interface BillingManager {
    val sharedFlow: Flow<Int>
    val stateFlow: Flow<List<BillingState>>
    fun initBillingClient()
    fun launchBillingFlow(activity: Activity, index: Int)
}