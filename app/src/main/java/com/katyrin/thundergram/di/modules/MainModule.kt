package com.katyrin.thundergram.di.modules

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.SimpleExoPlayer
import com.katyrin.thundergram.R
import com.katyrin.thundergram.di.ViewModelFactory
import com.katyrin.thundergram.di.ViewModelKey
import com.katyrin.thundergram.model.repository.MainRepository
import com.katyrin.thundergram.model.repository.MainRepositoryImpl
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.model.storage.StorageImpl
import com.katyrin.thundergram.view.main.MainActivity
import com.katyrin.thundergram.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Singleton
import android.os.Build
import com.katyrin.thundergram.BuildConfig


@Module
interface MainModule {

    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @Singleton
    fun bindMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository

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
                useMessageDatabase = true
                useSecretChats = false
                apiId = BuildConfig.APP_ID
                apiHash = BuildConfig.APP_HASH
                useFileDatabase = true
                systemLanguageCode = context.getString(R.string.system_language)
                deviceModel = getDeviceName()
                systemVersion = "Thundergram"
                applicationVersion = "1.0"
                enableStorageOptimizer = true
            }

        private fun getDeviceName(): String {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.lowercase().startsWith(manufacturer.lowercase())) capitalize(model)
            else capitalize(manufacturer) + " " + capitalize(model)
        }


        private fun capitalize(s: String?): String = when {
            s == null || s.isEmpty() -> ""
            Character.isUpperCase(s[0]) -> s
            else -> Character.toUpperCase(s[0]).toString() + s.substring(1)
        }

        @Provides
        @Singleton
        fun providePlayer(context: Context): SimpleExoPlayer =
            SimpleExoPlayer.Builder(context).build()
    }
}