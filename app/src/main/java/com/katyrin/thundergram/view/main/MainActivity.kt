package com.katyrin.thundergram.view.main

import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.katyrin.thundergram.BuildConfig
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.ActivityMainBinding
import com.katyrin.thundergram.utils.checkCallPermission
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.viewmodel.MainViewModel
import com.katyrin.thundergram.viewmodel.appstates.UserState
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), CallListener, ToolBarMotionListener, LoginListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels(factoryProducer = { factory })

    private var navController: NavController? = null
    private var navGraph: NavGraph? = null
    private var binding: ActivityMainBinding? = null
    private var mRewardedAd: RewardedAd? = null

    private val rewardedAdLoadCallback = object : RewardedAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            binding?.adsButton?.isEnabled = false
            mRewardedAd = null
        }

        override fun onAdLoaded(rewardedAd: RewardedAd) {
            binding?.adsButton?.isEnabled = true
            mRewardedAd = rewardedAd
        }
    }

    private val fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdShowedFullScreenContent() {
            loadRewardedAd()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
            loadRewardedAd()
        }

        override fun onAdDismissedFullScreenContent() {
            mRewardedAd = null
            loadRewardedAd()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        setNavigation()
        viewModel.liveData.observe(this, ::renderUserState)
        viewModel.checkLogin()
        initAds()
        initViews()
    }

    private fun initViews() {
        binding?.chatNameTextView?.text = getString(R.string.app_name)
        binding?.adsButton?.setOnClickListener { showRewardedAd() }
    }

    private fun initAds() {
        MobileAds.initialize(this) {}
        RequestConfiguration.Builder().setTestDeviceIds(listOf(MY_ADS_PHONE_ID))
        loadRewardedAd()
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            this,
            BuildConfig.AD_UNIT_ID,
            AdRequest.Builder().build(),
            rewardedAdLoadCallback
        )
        mRewardedAd?.fullScreenContentCallback = fullScreenContentCallback
    }

    private fun showRewardedAd() {
        if (mRewardedAd != null) mRewardedAd?.show(this) { onUserEarnedReward() }
        else toast(getString(R.string.ads_not_ready))
    }

    private fun onUserEarnedReward() {
        viewModel.saveCoins(getCurrentCoins() + ONE_COIN)
        loadRewardedAd()
    }

    private fun getCurrentCoins(): Long = binding?.countTextView?.text.toString().toLong()

    private fun setNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        navGraph = navController?.navInflater?.inflate(R.navigation.nav_graph)
    }

    private fun renderUserState(userState: UserState): Unit? = when (userState) {
        is UserState.Error -> userState.message?.let { toast(it) } ?: Unit
        is UserState.LoggedState -> setLoggedState()
        is UserState.NotLoggedState -> openScreen(R.id.loginFragment)
        is UserState.UpdateCoinState -> updateCoinCount(userState.currentCoin)
        is UserState.CallState -> onPhoneCallNumber(userState.phoneNumber, userState.decrementCoins)
    }

    private fun setLoggedState() {
        onLoginState()
        openScreen(R.id.chatListFragment)
    }

    private fun openScreen(screen: Int): Unit? = navGraph?.let { navGraph ->
        navGraph.startDestination = screen
        navController?.graph = navGraph
    }

    private fun updateCoinCount(currentCoin: Int) {
        binding?.countTextView?.text = currentCoin.toString()
    }

    override fun onPhoneCallNumber(phoneNumber: String, decrementCoin: Int) {
        val newCoins: Long = getCurrentCoins() - decrementCoin
        if (newCoins >= ZERO_COINS) callNumber(phoneNumber, newCoins)
        else toast(getString(R.string.lack_count_message))
    }

    private fun callNumber(phoneNumber: String, newCoins: Long): Unit = checkCallPermission {
        viewModel.saveCoins(newCoins)
        startActivity(Intent(ACTION_CALL, Uri.parse(phoneNumber)))
    }

    override fun onChangeSceneTransitionSwipe(dragDirection: Int) {
        binding?.motionLayout?.scene?.getTransitionById(R.id.app_bar_transition)?.setOnSwipe(
            OnSwipe()
                .setDragDirection(dragDirection)
                .setTouchAnchorId(R.id.main_container)
                .setTouchAnchorSide(OnSwipe.SIDE_TOP)
                .setMoveWhenScrollAtTop(false)
        )
    }

    override fun onSetToolBarText(text: String) {
        binding?.chatNameTextView?.text = text
    }

    override fun onLoginState() {
        viewModel.updateCoins()
        viewModel.callSubscribedPhone()
        viewModel.getUpdatesCoins()
    }

    override fun onDestroy() {
        navController = null
        navGraph = null
        binding = null
        mRewardedAd = null
        viewModel.cancelJob()
        super.onDestroy()
    }

    private companion object {
        const val ZERO_COINS = 0
        const val ONE_COIN = 1
        const val MY_ADS_PHONE_ID = "AB3034792ED476712252E3AE416A3296"
        const val FAKE_ADS_KEY = "ca-app-pub-3940256099942544/5224354917"
    }
}