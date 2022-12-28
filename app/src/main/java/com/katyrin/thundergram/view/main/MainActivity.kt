package com.katyrin.thundergram.view.main

import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.katyrin.thundergram.R
import com.katyrin.thundergram.ad.AdManager
import com.katyrin.thundergram.billing.BillingManager
import com.katyrin.thundergram.databinding.ActivityMainBinding
import com.katyrin.thundergram.utils.checkCallPermission
import com.katyrin.thundergram.utils.setTextRate
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.view.notification.worker.NotificationWorkerGenerator
import com.katyrin.thundergram.viewmodel.MainViewModel
import com.katyrin.thundergram.viewmodel.appstates.SoundEffect
import com.katyrin.thundergram.viewmodel.appstates.SoundState
import com.katyrin.thundergram.viewmodel.appstates.UserState
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), CallListener, ToolBarMotionListener, LoginListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels(factoryProducer = { factory })

    private var binding: ActivityMainBinding? = null

    private var navController: NavController? = null
    private var navGraph: NavGraph? = null

    @Inject
    lateinit var billingManager: BillingManager

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var notificationWorkerGenerator: NotificationWorkerGenerator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        setNavigation()
        initViewModel()
        initViews()
        initAds(savedInstanceState)
        initBilling(savedInstanceState)
    }

    private fun initViewModel() {
        viewModel.liveData.observe(this, ::renderUserState)
        viewModel.checkLogin()
        viewModel.soundEffect
            .flowWithLifecycle(lifecycle)
            .onEach(::renderSoundEffect)
            .launchIn(lifecycleScope)
        viewModel.soundState
            .flowWithLifecycle(lifecycle)
            .onEach(::renderSoundState)
            .launchIn(lifecycleScope)
    }

    private fun renderSoundEffect(soundEffect: SoundEffect): Unit = when (soundEffect) {
        is SoundEffect.OnSoundButtonPlay -> onSoundAnimation(R.drawable.avd_play_to_pause)
        is SoundEffect.OnSoundButtonPause -> onSoundAnimation(R.drawable.avd_pause_to_play)
    }

    private fun onSoundAnimation(drawableResource: Int) {
        binding?.apply {
            val appCompatDrawable =
                AppCompatResources.getDrawable(this@MainActivity, drawableResource)
            playButton.setImageDrawable(appCompatDrawable)
            val drawable: Drawable = playButton.drawable
            if (drawable is AnimatedVectorDrawable) drawable.start()
        }
    }

    private fun renderSoundState(soundState: SoundState) {
        binding?.soundLayout?.isVisible = soundState.isShowSoundBar
        binding?.soundName?.text = soundState.soundName
        binding?.soundSpeed?.setTextRate(soundState.speed)
        val drawableResource: Int =
            if (soundState.isPlayState) R.drawable.avd_play_to_pause else R.drawable.avd_pause_to_play
        onSoundAnimation(drawableResource)
    }

    private fun initBilling(savedInstanceState: Bundle?) {
        savedInstanceState ?: billingManager.initBillingClient()
        billingManager.sharedFlow
            .flowWithLifecycle(lifecycle)
            .onEach { viewModel.saveCoins(getCurrentCoins() + it) }
            .launchIn(lifecycleScope)
    }

    private fun initAds(savedInstanceState: Bundle?) {
        savedInstanceState ?: adManager.initAds(this)
        adManager.isPersonalizationAds = true
    }

    private fun initViews() {
        binding?.chatNameTextView?.text = getString(R.string.app_name)
        binding?.payButton?.setOnClickListener { navController?.navigate(R.id.billingDialogFragment) }
        binding?.adsButton?.setOnClickListener {
            adManager.tryShowRewardedVideo(this) {
                viewModel.saveCoins(getCurrentCoins() + ONE_COIN)
            }
        }
        binding?.playButton?.setOnClickListener { viewModel.onClickSoundButton() }
        binding?.btnExit?.setOnClickListener { viewModel.onClickSoundExit() }
        binding?.soundSpeed?.setOnClickListener { viewModel.onUpdateSoundSpeed() }
    }

    private fun setNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        navGraph = navController?.navInflater?.inflate(R.navigation.nav_graph)
    }

    private fun renderUserState(userState: UserState): Unit = when (userState) {
        is UserState.Error -> userState.message?.let { toast(it) } ?: Unit
        is UserState.LoggedState -> setLoggedState()
        is UserState.NotLoggedState -> setNotLoggedState()
        is UserState.UpdateCoinState -> updateCoinCount(userState.currentCoin)
        is UserState.CallState -> onPhoneCallNumber(userState.phoneNumber, userState.decrementCoins)
    }

    private fun setLoggedState() {
        onLoginState()
        openScreen(R.id.chatListFragment)
    }

    private fun setNotLoggedState() {
        binding?.motionLayout?.transitionToEnd()
        openScreen(R.id.tutorialFragment)
    }

    private fun openScreen(screen: Int): Unit? = navGraph?.let { navGraph ->
        navGraph.setStartDestination(screen)
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
        binding?.motionLayout?.transitionToStart()
        viewModel.updateCoins()
        viewModel.callSubscribedPhone()
        viewModel.getUpdatesCoins()
        notificationWorkerGenerator.startNotificationWork()
    }

    private fun getCurrentCoins(): Long = binding?.countTextView?.text.toString().toLong()

    override fun onDestroy() {
        navController = null
        navGraph = null
        super.onDestroy()
    }

    private companion object {
        const val ZERO_COINS = 0
        const val ONE_COIN = 1
    }
}