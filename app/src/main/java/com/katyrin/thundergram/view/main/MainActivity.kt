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
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.ActivityMainBinding
import com.katyrin.thundergram.utils.checkCallPermission
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.viewmodel.MainViewModel
import com.katyrin.thundergram.viewmodel.appstates.UserState
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), CallListener, ToolBarMotionListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels(factoryProducer = { factory })

    private var navController: NavController? = null
    private var navGraph: NavGraph? = null
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setNavigation()
        binding?.chatNameTextView?.text = getString(R.string.app_name)
        viewModel.liveData.observe(this, ::renderUserState)
        viewModel.callSubscribedPhone.observe(this, ::onPhoneCallNumber)
    }

    private fun setNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        navGraph = navController?.navInflater?.inflate(R.navigation.nav_graph)
    }

    private fun renderUserState(userState: UserState): Unit? = when (userState) {
        is UserState.Error -> userState.message?.let { toast(it) } ?: Unit
        is UserState.LoggedState -> openScreen(R.id.chatListFragment)
        is UserState.NotLoggedState -> openScreen(R.id.loginFragment)
    }

    private fun openScreen(screen: Int): Unit? =
        navGraph?.let { navGraph ->
            navGraph.startDestination = screen
            navController?.graph = navGraph
        }

    override fun onPhoneCallNumber(phoneNumber: String): Unit =
        checkCallPermission { startActivity(Intent(ACTION_CALL, Uri.parse(phoneNumber))) }

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

    override fun onDestroy() {
        navController = null
        navGraph = null
        binding = null
        super.onDestroy()
    }
}