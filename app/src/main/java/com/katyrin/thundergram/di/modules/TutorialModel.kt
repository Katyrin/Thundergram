package com.katyrin.thundergram.di.modules

import com.katyrin.thundergram.view.tutorial.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface TutorialModel {

    @ContributesAndroidInjector
    fun contributeTutorialMainFragment(): TutorialMainFragment

    @ContributesAndroidInjector
    fun contributeTutorialMainPage0Fragment(): TutorialMainPage0Fragment

    @ContributesAndroidInjector
    fun contributeTutorialMainPage1Fragment(): TutorialMainPage1Fragment

    @ContributesAndroidInjector
    fun contributeTutorialMainPage2Fragment(): TutorialMainPage2Fragment

    @ContributesAndroidInjector
    fun contributeTutorialMainPage3Fragment(): TutorialMainPage3Fragment

    @ContributesAndroidInjector
    fun contributeTutorialMainPage4Fragment(): TutorialMainPage4Fragment
}