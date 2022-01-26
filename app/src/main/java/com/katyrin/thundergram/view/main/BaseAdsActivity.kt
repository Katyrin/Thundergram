package com.katyrin.thundergram.view.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.katyrin.thundergram.BuildConfig.AD_UNIT_ID
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.ActivityMainBinding
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.viewmodel.MainViewModel
import javax.inject.Inject

abstract class BaseAdsActivity : BaseBillingActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    protected val viewModel: MainViewModel by viewModels(factoryProducer = { factory })

    protected var binding: ActivityMainBinding? = null
    private var mRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(listOf(MY_ADS_PHONE_ID))
        MobileAds.setRequestConfiguration(configuration.build())
        MobileAds.initialize(this) { loadRewardedAd() }
        binding?.adsButton?.setOnClickListener { showRewardedAd() }
    }

    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this, AD_UNIT_ID, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                super.onAdFailedToLoad(adError)
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                super.onAdLoaded(rewardedAd)
                mRewardedAd = rewardedAd
            }
        })
    }

    private fun showRewardedAd() {
        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                mRewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                super.onAdFailedToShowFullScreenContent(adError)
                loadRewardedAd()
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                loadRewardedAd()
            }
        }
        if (mRewardedAd == null) onAdNotLoaded()
        else mRewardedAd?.show(this) { viewModel.saveCoins(getCurrentCoins() + ONE_COIN) }
    }

    private fun onAdNotLoaded() {
        toast(R.string.ads_not_ready)
        loadRewardedAd()
    }

    protected fun getCurrentCoins(): Long = binding?.countTextView?.text.toString().toLong()

    override fun onDestroy() {
        mRewardedAd = null
        binding = null
        viewModel.cancelJob()
        super.onDestroy()
    }

    private companion object {
        const val ONE_COIN = 1
        const val MY_ADS_PHONE_ID = "AB3034792ED476712252E3AE416A3296"
        const val FAKE_ADS_KEY = "ca-app-pub-3940256099942544/5224354917"
    }
}