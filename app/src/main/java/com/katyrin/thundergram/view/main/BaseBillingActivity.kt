package com.katyrin.thundergram.view.main

import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.android.billingclient.api.BillingClient.BillingResponseCode.USER_CANCELED
import com.android.billingclient.api.Purchase.PurchaseState.PURCHASED
import com.katyrin.thundergram.R
import com.katyrin.thundergram.utils.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseBillingActivity : DaggerAppCompatActivity() {

    var billingClient: BillingClient? = null

    private fun purchasesUpdatedListener(increaseCoin: (Int) -> Unit): PurchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, list ->
            when {
                billingResult.responseCode == OK && !list.isNullOrEmpty() ->
                    for (purchase in list) verifyPurchase(purchase) { increaseCoin(it) }
                billingResult.responseCode == USER_CANCELED -> toast(R.string.billing_canceling)
                else -> toast(R.string.any_other_error)
            }
        }

    private fun verifyPurchase(purchase: Purchase, increaseCoin: (Int) -> Unit) {
        lifecycleScope.launch {
            when (purchase.skus[0]) {
                CALL_50_COIN -> handlePurchase(purchase) { increaseCoin(COIN_50) }
                CALL_100_COIN -> handlePurchase(purchase) { increaseCoin(COIN_100) }
                CALL_500_COIN -> handlePurchase(purchase) { increaseCoin(COIN_500) }
            }
        }
    }

    private suspend fun handlePurchase(purchase: Purchase, increaseCoin: () -> Unit) {
        val consumeParams =
            ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        val consumeResult = withContext(Dispatchers.IO) {
            billingClient?.consumePurchase(consumeParams)
        }
        if (consumeResult?.billingResult?.responseCode == OK) increaseCoin()
    }

    protected fun initBillingClient(increaseCoin: (Int) -> Unit) {
        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener(purchasesUpdatedListener { increaseCoin(it) })
            .build()
    }

    protected fun checkBillingPurchased(increaseCoin: (Int) -> Unit) {
        billingClient?.queryPurchasesAsync(BillingClient.SkuType.INAPP) { billingResult, list ->
            if (billingResult.responseCode == OK && !list.isNullOrEmpty())
                for (purchase in list)
                    if (purchase.purchaseState == PURCHASED && !purchase.isAcknowledged)
                        verifyPurchase(purchase) { increaseCoin(it) }
        }
    }

    override fun onDestroy() {
        billingClient = null
        super.onDestroy()
    }
}