package com.katyrin.thundergram.di.modules

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.katyrin.thundergram.R
import com.katyrin.thundergram.di.ViewModelFactory
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.model.storage.StorageImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Singleton

@Module
interface MainModule {

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
                databaseDirectory = "/data/user/0/com.katyrin.thundergram/files/td"
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
    }
}