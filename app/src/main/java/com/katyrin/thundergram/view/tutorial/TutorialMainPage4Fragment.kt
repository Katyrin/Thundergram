package com.katyrin.thundergram.view.tutorial

import android.view.LayoutInflater
import android.view.ViewGroup
import com.katyrin.thundergram.databinding.FragmentTutorialMainPage4Binding
import com.katyrin.thundergram.view.BaseFragment

class TutorialMainPage4Fragment : BaseFragment<FragmentTutorialMainPage4Binding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTutorialMainPage4Binding =
        FragmentTutorialMainPage4Binding.inflate(inflater, container, false)
}