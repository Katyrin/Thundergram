package com.katyrin.thundergram.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.katyrin.thundergram.databinding.FragmentBillingDialogBinding
import com.katyrin.thundergram.utils.CALL_100_COIN
import com.katyrin.thundergram.utils.CALL_500_COIN
import com.katyrin.thundergram.utils.CALL_50_COIN
import com.katyrin.thundergram.view.main.BaseBillingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BillingDialogFragment : BottomSheetDialogFragment() {

    private var binding: FragmentBillingDialogBinding? = null
    private var billingClient: BillingClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBillingDialogBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        billingClient = (requireActivity() as BaseBillingActivity).billingClient
        connectToGooglePlayBilling()
    }

    private fun connectToGooglePlayBilling() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                connectToGooglePlayBilling()
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == OK) lifecycleScope.launch { getProductDetails() }
            }
        })
    }

    private suspend fun getProductDetails() {
        val params = SkuDetailsParams.newBuilder()
        val skuList = arrayListOf(CALL_50_COIN, CALL_100_COIN, CALL_500_COIN)
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        val skuDetailsResult = withContext(Dispatchers.IO) {
            billingClient?.querySkuDetails(params.build())
        }
        val list = skuDetailsResult?.skuDetailsList
        val responseCode = skuDetailsResult?.billingResult?.responseCode
        if (responseCode == OK && !list.isNullOrEmpty()) initViews(list)
    }

    private suspend fun initViews(skuDetailsList: List<SkuDetails>): Unit =
        withContext(Dispatchers.Main) {
            val skuTitleAppNameRegex = """(?> \(.+?\))$""".toRegex()
            var call50Coin: SkuDetails? = null
            var call100Coin: SkuDetails? = null
            var call500Coin: SkuDetails? = null
            for (skuDetails in skuDetailsList)
                when (skuDetails.sku) {
                    CALL_50_COIN -> call50Coin = skuDetails
                    CALL_100_COIN -> call100Coin = skuDetails
                    CALL_500_COIN -> call500Coin = skuDetails
                }
            binding?.apply {
                call50Coin?.apply {
                    firstBillingText.text = title.replace(skuTitleAppNameRegex, "")
                    firstBillingButton.text = price
                    firstBillingButton.setOnClickListener { setButtonBillingClick(this) }
                }
                call100Coin?.apply {
                    secondBillingText.text = title.replace(skuTitleAppNameRegex, "")
                    secondBillingButton.text = price
                    secondBillingButton.setOnClickListener { setButtonBillingClick(this) }
                }
                call500Coin?.apply {
                    thirdBillingText.text = title.replace(skuTitleAppNameRegex, "")
                    thirdBillingButton.text = price
                    thirdBillingButton.setOnClickListener { setButtonBillingClick(this) }
                }
            }
        }

    private fun setButtonBillingClick(skuDetails: SkuDetails) {
        val flowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
        billingClient?.launchBillingFlow(requireActivity(), flowParams)
        dismiss()
    }

    override fun onDestroyView() {
        binding = null
        billingClient = null
        super.onDestroyView()
    }
}