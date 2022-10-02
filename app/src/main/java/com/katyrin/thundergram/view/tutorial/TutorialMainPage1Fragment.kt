package com.katyrin.thundergram.view.tutorial

import android.view.LayoutInflater
import android.view.ViewGroup
import com.katyrin.thundergram.databinding.FragmentTutorialMainPage1Binding
import com.katyrin.thundergram.view.BaseFragment

class TutorialMainPage1Fragment : BaseFragment<FragmentTutorialMainPage1Binding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTutorialMainPage1Binding =
        FragmentTutorialMainPage1Binding.inflate(inflater, container, false)
}