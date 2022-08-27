package com.katyrin.thundergram.di

import android.content.Context
import com.katyrin.thundergram.App
import com.katyrin.thundergram.di.modules.ChatListModule
import com.katyrin.thundergram.di.modules.ChatModule
import com.katyrin.thundergram.di.modules.LoginModule
import com.katyrin.thundergram.di.modules.MainModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        LoginModule::class,
        MainModule::class,
        ChatListModule::class,
        ChatModule::class,
        BillingModule::class,
        AdsModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withContext(context: Context): Builder

        fun build(): AppComponent
    }
}