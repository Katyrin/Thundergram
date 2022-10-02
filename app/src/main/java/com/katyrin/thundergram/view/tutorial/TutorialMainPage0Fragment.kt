package com.katyrin.thundergram.view.tutorial

import android.view.LayoutInflater
import android.view.ViewGroup
import com.katyrin.thundergram.databinding.FragmentTutorialMainPage0Binding
import com.katyrin.thundergram.view.BaseFragment

class TutorialMainPage0Fragment : BaseFragment<FragmentTutorialMainPage0Binding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTutorialMainPage0Binding =
        FragmentTutorialMainPage0Binding.inflate(inflater, container, false)
}