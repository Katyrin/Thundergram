package com.katyrin.thundergram.di.modules

import com.katyrin.thundergram.billing.BillingManager
import com.katyrin.thundergram.billing.BillingManagerImpl
import com.katyrin.thundergram.view.BillingDialogFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
interface BillingModule {

    @ContributesAndroidInjector
    fun contributeBillingDialogFragment(): BillingDialogFragment

    @Binds
    @Singleton
    fun bindBillingManager(billingManagerImpl: BillingManagerImpl): BillingManager
}