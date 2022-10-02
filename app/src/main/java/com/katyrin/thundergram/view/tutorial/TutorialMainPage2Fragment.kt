package com.katyrin.thundergram.view.tutorial

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.FragmentTutorialMainPage2Binding
import com.katyrin.thundergram.view.BaseFragment

class TutorialMainPage2Fragment : BaseFragment<FragmentTutorialMainPage2Binding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTutorialMainPage2Binding =
        FragmentTutorialMainPage2Binding.inflate(inflater, container, false)

    override fun onResume() {
        super.onResume()
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.tutor_hand)
        binding?.hand?.startAnimation(animation)
    }
}