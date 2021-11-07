package com.katyrin.thundergram.di.modules

import androidx.lifecycle.ViewModel
import com.katyrin.thundergram.di.ViewModelKey
import com.katyrin.thundergram.model.repository.LoginRepository
import com.katyrin.thundergram.model.repository.LoginRepositoryImpl
import com.katyrin.thundergram.model.mapping.LoginMapping
import com.katyrin.thundergram.model.mapping.LoginMappingImpl
import com.katyrin.thundergram.view.LoginFragment
import com.katyrin.thundergram.viewmodel.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
interface LoginModule {

    @ContributesAndroidInjector
    fun bindLoginFragment(): LoginFragment

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @Singleton
    fun bindLoginMapping(loginMappingImpl: LoginMappingImpl): LoginMapping

    @Binds
    @Singleton
    fun bindLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    companion object {
        @Provides
        @Singleton
        fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Default
    }
}