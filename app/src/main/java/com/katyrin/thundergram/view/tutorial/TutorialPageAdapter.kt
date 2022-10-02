package com.katyrin.thundergram.view.tutorial

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TutorialPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = TOTAL_COUNT

    override fun createFragment(position: Int): Fragment = when (position) {
        FIRST_POSITION -> TutorialMainPage0Fragment()
        SECOND_POSITION -> TutorialMainPage1Fragment()
        THIRD_POSITION -> TutorialMainPage2Fragment()
        FOURTH_POSITION -> TutorialMainPage3Fragment()
        else -> TutorialMainPage4Fragment()
    }

    private companion object {
        const val TOTAL_COUNT = 5
        const val FIRST_POSITION = 0
        const val SECOND_POSITION = 1
        const val THIRD_POSITION = 2
        const val FOURTH_POSITION = 3
    }
}