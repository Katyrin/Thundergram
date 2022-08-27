package com.katyrin.thundergram.ad

import android.app.Activity
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.MobileAds
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.katyrin.thundergram.BuildConfig.AD_REWARD_ID_YANDEX
import com.katyrin.thundergram.R
import com.katyrin.thundergram.utils.toast
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import javax.inject.Inject

class AdManagerYandex @Inject constructor() : AdManager {

    private var mRewardedAd: RewardedAd? = null
    override var isPersonalizationAds: Boolean = false
    private val isRewardedLoaded: Boolean
        get() = mRewardedAd?.isLoaded == true

    override fun initAds(activity: Activity) {
        MobileAds.initialize(activity) { loadAdMobRewardedAd(activity) }
    }

    private fun loadAdMobRewardedAd(activity: Activity) {
        mRewardedAd = RewardedAd(activity)
        mRewardedAd?.setAdUnitId(AD_REWARD_ID_YANDEX)
        if (!isRewardedLoaded) mRewardedAd?.loadAd(getAdMobRequest())
    }

    private fun getAdMobRequest(): AdRequest = AdRequest.Builder().build()

    override fun tryShowRewardedVideo(activity: Activity, getReward: () -> Unit) {
        if (isRewardedLoaded) showRewardedVideo(activity, getReward)
        else activity.toast(R.string.ads_not_ready)
    }

    private fun showRewardedVideo(activity: Activity, getReward: () -> Unit) {
        mRewardedAd?.setRewardedAdEventListener(object : RewardedAdEventListener {
            override fun onAdLoaded() {}
            override fun onAdFailedToLoad(error: AdRequestError) {}
            override fun onAdShown() {
                mRewardedAd = null
            }

            override fun onAdDismissed(): Unit = loadAdMobRewardedAd(activity)
            override fun onRewarded(p0: Reward): Unit = getReward()
            override fun onAdClicked() {}
            override fun onLeftApplication() {}
            override fun onReturnedToApplication() {}
            override fun onImpression(p0: ImpressionData?) {}
        })
        mRewardedAd?.show()
    }
}