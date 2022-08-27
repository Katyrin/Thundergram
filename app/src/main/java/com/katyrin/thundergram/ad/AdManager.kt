package com.katyrin.thundergram.ad

import android.app.Activity

interface AdManager {
    var isPersonalizationAds: Boolean
    fun initAds(activity: Activity)
    fun tryShowRewardedVideo(activity: Activity, getReward: () -> Unit)
}