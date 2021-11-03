package com.katyrin.thundergram.di.modules

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.SimpleExoPlayer
import com.katyrin.thundergram.R
import com.katyrin.thundergram.di.ViewModelFactory
import com.katyrin.thundergram.di.ViewModelKey
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.model.storage.StorageImpl
import com.katyrin.thundergram.view.MainActivity
import com.katyrin.thundergram.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Singleton

@Module
interface MainModule {

    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @Singleton
    fun bindStorage(storageImpl: StorageImpl): Storage

    companion object {

        @Provides
        @Singleton
        @SuppressLint("SdCardPath")
        fun provideTdlibParameters(context: Context): TdApi.TdlibParameters =
            TdApi.TdlibParameters().apply {
                databaseDirectory = "/data/user/0/com.katyrin.thundergram.real/files/td"
                useMessageDatabase = false
                useSecretChats = false
                apiId = 7305022
                apiHash = "d40e10a9ae398f329eaf883961111d74"
                useFileDatabase = true
                systemLanguageCode = context.getString(R.string.system_language)
                deviceModel = "Android"
                systemVersion = "Thundergram"
                applicationVersion = "1.0"
                enableStorageOptimizer = true
            }

        @Provides
        @Singleton
        fun providePlayer(context: Context): SimpleExoPlayer =
            SimpleExoPlayer.Builder(context).build()
    }
}