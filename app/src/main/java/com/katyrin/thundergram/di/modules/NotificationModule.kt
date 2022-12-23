package com.katyrin.thundergram.di.modules

import com.katyrin.thundergram.view.notification.NotificationGenerator
import com.katyrin.thundergram.view.notification.NotificationGeneratorImpl
import com.katyrin.thundergram.view.notification.NotificationService
import com.katyrin.thundergram.view.notification.worker.NotificationWorkerGenerator
import com.katyrin.thundergram.view.notification.worker.NotificationWorkerGeneratorImpl
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
interface NotificationModule {

    @ContributesAndroidInjector
    fun contributeNotificationService(): NotificationService

    @Binds
    @Singleton
    fun bindNotificationGenerator(
        notificationGeneratorImpl: NotificationGeneratorImpl
    ): NotificationGenerator

    @Binds
    @Singleton
    fun bindNotificationWorkerGenerator(
        notificationWorkerGeneratorImpl: NotificationWorkerGeneratorImpl
    ): NotificationWorkerGenerator
}