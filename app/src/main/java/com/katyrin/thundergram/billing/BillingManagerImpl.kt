package com.katyrin.thundergram.billing

import android.app.Activity
import android.content.Context
import android.util.Base64
import android.util.Log
import com.android.billingclient.api.*
import com.katyrin.thundergram.R
import com.katyrin.thundergram.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import javax.inject.Inject

class BillingManagerImpl @Inject constructor(private val context: Context) : BillingManager {

    private val billingJob = SupervisorJob()
    private val lifecycleScope: CoroutineScope = CoroutineScope(Dispatchers.Main + billingJob)

    private var billingClient: BillingClient? = null

    private val productDetailsList: MutableList<ProductDetails?> = MutableList(3) { null }
    private val billingStateList: MutableList<BillingState> = MutableList(3) { BillingState() }

    private val _sharedFlow: MutableSharedFlow<Int> = MutableSharedFlow()
    override val sharedFlow: SharedFlow<Int> = _sharedFlow.asSharedFlow()
    private val _stateFlow: MutableStateFlow<List<BillingState>> =
        MutableStateFlow(billingStateList)
    override val stateFlow: StateFlow<List<BillingState>> = _stateFlow.asStateFlow()

    private val purchaseUpdateListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> purchases?.let { processPurchases(it.toSet()) }
            BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> Log.d(LOG_TAG, "Item Not Owned")
            BillingClient.BillingResponseCode.USER_CANCELED -> context.toast(R.string.billing_canceling)
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> connectToPlayBillingService()
            else -> context.toast(R.string.any_other_error)
        }
    }

    private val billingClientStateListener = object : BillingClientStateListener {
        override fun onBillingSetupFinished(billingResult: BillingResult): Unit =
            billingSetupFinished(billingResult)

        override fun onBillingServiceDisconnected() {
            Log.d(LOG_TAG, "onBillingServiceDisconnected")
            connectToPlayBillingService()
        }
    }

    override fun initBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener(purchaseUpdateListener)
            .build()
        connectToPlayBillingService()
        queryPurchases()
    }

    private fun connectToPlayBillingService(): Boolean =
        if (billingClient?.isReady == false) {
            lifecycleScope.launch(Dispatchers.Default) {
                billingClient?.startConnection(billingClientStateListener)
            }
            true
        } else false

    private fun billingSetupFinished(billingResult: BillingResult) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                Log.d(LOG_TAG, "onBillingSetupFinished successfully")
                querySkuDetails()
                queryPurchases()
            }
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE ->
                Log.d(LOG_TAG, billingResult.debugMessage)
            else -> Log.d(LOG_TAG, billingResult.debugMessage)
        }
    }

    private fun querySkuDetails() {
        val productList: MutableList<QueryProductDetailsParams.Product> = mutableListOf()
        arrayListOf(CALL_50_COIN, CALL_100_COIN, CALL_500_COIN).forEach { productId ->
            productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )
        }
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList)
        billingClient?.queryProductDetailsAsync(params.build()) { billingResult, productDetailsList ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    Log.d(LOG_TAG, "querySkuDetailsAsync = $productDetailsList")
                    productDetailsList.forEach { productDetails ->
                        when (productDetails.productId) {
                            CALL_50_COIN -> saveBillingState(productDetails, 0)
                            CALL_100_COIN -> saveBillingState(productDetails, 1)
                            CALL_500_COIN -> saveBillingState(productDetails, 2)
                        }
                    }
                    lifecycleScope.launch { _stateFlow.emit(billingStateList) }
                }
                else -> Log.e(LOG_TAG, billingResult.debugMessage)
            }
        }
    }

    private fun saveBillingState(productDetails: ProductDetails, index: Int) {
        productDetails.oneTimePurchaseOfferDetails?.let { oneTimePurchaseOfferDetails ->
            productDetailsList[index] = productDetails
            val skuTitleAppNameRegex = """(?> \(.+?\))$""".toRegex()
            val title = productDetails.title.replace(skuTitleAppNameRegex, "")
            billingStateList[index] =
                BillingState(title, oneTimePurchaseOfferDetails.formattedPrice)
        }
    }

    private fun queryPurchases() {
        Log.d(LOG_TAG, "queryPurchasesAsync called")
        billingClient?.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { _, list ->
            Log.d(LOG_TAG, "queryPurchasesAsync INAPP results: ${list.size}")
            processPurchases(list.toSet())
        }
        if (isSubscriptionSupported()) {
            billingClient?.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            ) { _, list ->
                Log.d(LOG_TAG, "queryPurchasesAsync SUBS results: ${list.size}")
                processPurchases(list.toSet())
            }
        }
    }

    private fun isSubscriptionSupported(): Boolean {
        val billingResult =
            billingClient?.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        var succeeded = false
        when (billingResult?.responseCode) {
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> connectToPlayBillingService()
            BillingClient.BillingResponseCode.OK -> succeeded = true
            else ->
                Log.w(LOG_TAG, "isSubscriptionSupported() error: ${billingResult?.debugMessage}")
        }
        return succeeded
    }

    private fun processPurchases(setPurchases: Set<Purchase>) {
        val validPurchases = HashSet<Purchase>(setPurchases.size)
        setPurchases.forEach { purchase ->
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED)
                if (purchase.isSignatureValid()) validPurchases.add(purchase)
        }
        acknowledgeNonConsumablePurchases(validPurchases.toList())
    }

    private fun acknowledgeNonConsumablePurchases(nonConsumables: List<Purchase>) {
        nonConsumables.forEach { purchase ->
            if (!purchase.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                billingClient?.acknowledgePurchase(params) { billingResult ->
                    when (billingResult.responseCode) {
                        BillingClient.BillingResponseCode.OK -> purchase.disburseNonConsumableEntitlement()
                        else -> Log.d(
                            LOG_TAG,
                            "acknowledgeNonConsumablePurchasesAsync response is ${billingResult.debugMessage}"
                        )
                    }
                }
            } else purchase.disburseNonConsumableEntitlement()
        }
    }

    private fun Purchase.disburseNonConsumableEntitlement(): Unit = when (products[0]) {
        CALL_50_COIN -> handlePurchase(this, COIN_50)
        CALL_100_COIN -> handlePurchase(this, COIN_100)
        CALL_500_COIN -> handlePurchase(this, COIN_500)
        else -> Unit
    }

    private fun handlePurchase(purchase: Purchase, coin: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val consumeParams =
                ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
            val consumeResult = billingClient?.consumePurchase(consumeParams)
            if (consumeResult?.billingResult?.responseCode == BillingClient.BillingResponseCode.OK)
                _sharedFlow.emit(coin)
        }
    }

    private fun Purchase.isSignatureValid(): Boolean =
        if (originalJson.isEmpty() || signature.isEmpty()) false
        else verify(originalJson, signature)

    private fun verify(signedData: String, signature: String?): Boolean = try {
        val signatureAlgorithm = Signature.getInstance(SHA_RSA_ALGORITHM)
        signatureAlgorithm.initVerify(generatePublicKey())
        signatureAlgorithm.update(signedData.toByteArray())
        signatureAlgorithm.verify(Base64.decode(signature, Base64.DEFAULT))
    } catch (exception: NoSuchAlgorithmException) { // "RSA" is guaranteed to be available.
        throw RuntimeException(exception)
    } catch (exception: InvalidKeyException) {
        false
    } catch (exception: SignatureException) {
        false
    } catch (exception: IllegalArgumentException) {
        false
    }

    @Throws(IOException::class)
    private fun generatePublicKey(): PublicKey = try {
        val decodedKey = Base64.decode(BASE_64_ENCODED_PUBLIC_KEY, Base64.DEFAULT)
        val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
        keyFactory.generatePublic(X509EncodedKeySpec(decodedKey))
    } catch (exception: NoSuchAlgorithmException) { // "RSA" is guaranteed to be available.
        throw RuntimeException(exception)
    } catch (exception: InvalidKeySpecException) {
        throw IOException("Invalid key specification: $exception")
    }

    override fun launchBillingFlow(activity: Activity, index: Int) {
        productDetailsList[index]?.let { productDetails ->
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
            )
            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()
            billingClient?.launchBillingFlow(activity, billingFlowParams)
        }
    }

    private companion object {
        const val LOG_TAG = "Billing"
        const val BASE_64_ENCODED_PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkN+Yw8XdAEVNa5wAzZ1smoVZqaFyqFQ8OWGcW8RRyRETtrj4Nos619SAoZWLGnL0y2i3zrnVzbA5sNwruvuoz1LTTci6iaF95D1Htc+BwHR9kHZ6JiYUcz5gFOuAC1Yo9B/Pc9UrqEEaMJybD4wYwxdmNEYpWFqd78WENlNNLFLO9eSxuU0qHTgUPe4kuWk5Q503QEtaqBYkVNtVvTEk8tAoGZRUQG5anYQDoi4PlydB1QDNjyoQAyXZQbIaQkc5/79ijFibsq/ikXZZD+m+n1eHd6TAItQkuJgzhzEmPYCMMh/B9ZHuDVfUXwhqKWSaQ9JXj8mgbkSJq3Yp1Qe6xQIDAQAB"
        const val RSA_ALGORITHM = "RSA"
        const val SHA_RSA_ALGORITHM = "SHA1withRSA"
    }
}