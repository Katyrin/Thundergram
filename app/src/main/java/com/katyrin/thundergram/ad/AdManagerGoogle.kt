package com.katyrin.thundergram.ad

import android.app.Activity
import android.os.Bundle
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.katyrin.thundergram.BuildConfig.AD_REWARD_ID_GOOGLE
import com.katyrin.thundergram.R
import com.katyrin.thundergram.utils.toast
import javax.inject.Inject

class AdManagerGoogle @Inject constructor() : AdManager {

    private var mRewardedAd: RewardedAd? = null
    override var isPersonalizationAds: Boolean = false

    override fun initAds(activity: Activity) {
        MobileAds.initialize(activity) { loadAdMobRewardedAd(activity) }
        setTestDevice()
    }

    private fun setTestDevice() {
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(listOf(MY_ADS_PHONE_ID))
        MobileAds.setRequestConfiguration(configuration.build())
    }

    private fun loadAdMobRewardedAd(activity: Activity): Unit = RewardedAd.load(
        activity,
        AD_REWARD_ID_GOOGLE,
        getAdMobRequest(),
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                super.onAdFailedToLoad(adError)
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                super.onAdLoaded(rewardedAd)
                mRewardedAd = rewardedAd
            }
        })

    private fun getAdMobRequest(): AdRequest =
        if (isPersonalizationAds) AdRequest.Builder().build()
        else AdRequest.Builder().addNetworkExtrasBundle(
            AdMobAdapter::class.java,
            Bundle().apply { putString("npa", "1") }
        ).build()

    override fun tryShowRewardedVideo(activity: Activity, getReward: () -> Unit) {
        if (mRewardedAd != null) showRewardedVideo(activity, getReward)
        else activity.toast(R.string.ads_not_ready)
    }

    private fun showRewardedVideo(activity: Activity, getReward: () -> Unit) {
        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                loadAdMobRewardedAd(activity)
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                mRewardedAd = null
            }
        }
        mRewardedAd?.show(activity) { getReward() }
    }

    private companion object {
        const val MY_ADS_PHONE_ID = "AB3034792ED476712252E3AE416A3296"
        const val FAKE_ADS_KEY = "ca-app-pub-3940256099942544/5224354917"
    }
}