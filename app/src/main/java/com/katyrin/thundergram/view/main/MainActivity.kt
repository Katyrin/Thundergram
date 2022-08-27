package com.katyrin.thundergram.view.main

import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.work.*
import com.katyrin.thundergram.R
import com.katyrin.thundergram.ad.AdManager
import com.katyrin.thundergram.billing.BillingManager
import com.katyrin.thundergram.databinding.ActivityMainBinding
import com.katyrin.thundergram.utils.checkCallPermission
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.view.notification.worker.NotificationWorker
import com.katyrin.thundergram.viewmodel.MainViewModel
import com.katyrin.thundergram.viewmodel.appstates.UserState
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        setNavigation()
        viewModel.liveData.observe(this, ::renderUserState)
        viewModel.checkLogin()
        initViews()
        initAds(savedInstanceState)
        initBilling(savedInstanceState)
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
    }

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
        viewModel.updateCoins()
        viewModel.callSubscribedPhone()
        viewModel.getUpdatesCoins()
        startNotificationWork()
    }

    private fun startNotificationWork() {
        if (isWorkScheduled()) WorkManager.getInstance(this).cancelAllWorkByTag(TAG_NOTIFY_WORK)
        val keep = ExistingPeriodicWorkPolicy.KEEP
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(TAG_NOTIFY_WORK, keep, getPeriodicWorkRequest())
    }

    private fun getPeriodicWorkRequest(): PeriodicWorkRequest =
        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            .let { constraints ->
                PeriodicWorkRequest.Builder(
                    NotificationWorker::class.java,
                    REPEAT_INTERVAL, TimeUnit.MINUTES,
                    FIX_INTERVAL, TimeUnit.MINUTES
                )
                    .addTag(TAG_NOTIFY_WORK)
                    .setConstraints(constraints)
                    .build()
            }

    private fun isWorkScheduled(): Boolean = try {
        var running = false
        WorkManager.getInstance(this).getWorkInfosByTag(TAG_NOTIFY_WORK).get().forEach {
            running = it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED
        }
        running
    } catch (e: ExecutionException) {
        e.printStackTrace()
        false
    } catch (e: InterruptedException) {
        e.printStackTrace()
        false
    }

    private fun getCurrentCoins(): Long = binding?.countTextView?.text.toString().toLong()

    override fun onDestroy() {
        navController = null
        navGraph = null
        super.onDestroy()
    }

    private companion object {
        const val ZERO_COINS = 0
        const val TAG_NOTIFY_WORK = "TAG_NOTIFY_WORK"
        const val FIX_INTERVAL = 25L
        const val REPEAT_INTERVAL = 30L
        const val ONE_COIN = 1
    }
}