package com.katyrin.thundergram.di.modules

import androidx.lifecycle.ViewModel
import com.katyrin.thundergram.di.ViewModelKey
import com.katyrin.thundergram.view.UserMenuDialogFragment
import com.katyrin.thundergram.viewmodel.UserMenuViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface UserMenuModule {

    @ContributesAndroidInjector
    fun bindUserMenuDialogFragment(): UserMenuDialogFragment

    @Binds
    @IntoMap
    @ViewModelKey(UserMenuViewModel::class)
    fun bindUserMenuViewModel(viewModel: UserMenuViewModel): ViewModel
}