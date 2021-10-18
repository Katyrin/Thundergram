package com.katyrin.thundergram

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.thundergram.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent
            .builder()
            .withContext(applicationContext)
            .withTelegramFlow(TelegramFlow().apply { attachClient() })
            .build()
}