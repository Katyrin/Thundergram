package com.katyrin.thundergram.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.katyrin.thundergram.R
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.viewmodel.MainViewModel
import com.katyrin.thundergram.viewmodel.appstates.UserState
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels(factoryProducer = { factory })

    private var navController: NavController? = null
    private var navGraph: NavGraph? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigation()
        viewModel.liveData.observe(this, ::renderUserState)
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

    override fun onDestroy() {
        navController = null
        navGraph = null
        super.onDestroy()
    }
}