package com.katyrin.thundergram.view.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.NavDirections
import androidx.viewpager2.widget.ViewPager2
import com.katyrin.thundergram.databinding.FragmentTutorialMainBinding
import com.katyrin.thundergram.view.BaseFragment

class TutorialMainFragment : BaseFragment<FragmentTutorialMainBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTutorialMainBinding =
        FragmentTutorialMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding?.apply {
            btnSkip.setOnClickListener { openLoginScreen() }
            pages.setPageTransformer(ZoomOutPageTransformer())
            pages.adapter = TutorialPageAdapter(this@TutorialMainFragment)
            pages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    pageIndicatorView.selection = position
                    btnSkip.isVisible = position != FIFTH_PAGE
                    btnNext.setOnClickListener {
                        if (position == FIFTH_PAGE) openLoginScreen()
                        else pages.currentItem = pages.currentItem + ONE_PAGE
                    }
                }
            })
        }
    }

    private fun openLoginScreen() {
        val navDirections: NavDirections =
            TutorialMainFragmentDirections.actionTutorialFragmentToLoginFragment()
        navController?.navigate(navDirections)
    }

    private companion object {
        const val ONE_PAGE = 1
        const val FIFTH_PAGE = 4
    }
}