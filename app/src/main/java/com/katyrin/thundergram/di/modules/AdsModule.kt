package com.katyrin.thundergram.di.modules

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import com.katyrin.thundergram.ad.AdManager
import com.katyrin.thundergram.ad.AdManagerGoogle
import com.katyrin.thundergram.ad.AdManagerYandex
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class AdsModule {

    @Provides
    @Singleton
    fun provideAdManager(context: Context): AdManager =
        when (context.getDetectedCountry()) {
            RU, BE -> AdManagerYandex()
            else -> AdManagerGoogle()
        }

    private fun Context.getDetectedCountry(): String =
        detectNetworkCountry() ?: detectSIMCountry() ?: detectLocaleCountry()

    private fun Context.detectNetworkCountry(): String? = try {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkCountryIso = telephonyManager.networkCountryIso
        Log.d(TAG, "detectNetworkCountry: $networkCountryIso")
        if (networkCountryIso == "") null else networkCountryIso
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private fun Context.detectSIMCountry(): String? = try {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simCountryIso = telephonyManager.simCountryIso
        Log.d(TAG, "detectSIMCountry: $simCountryIso")
        if (simCountryIso == "") null else simCountryIso
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private fun detectLocaleCountry(): String = try {
        val localeCountryISO = Locale.getDefault().language
        Log.d(TAG, "detectLocaleCountry: $localeCountryISO")
        if (localeCountryISO == "") DEFAULT else localeCountryISO
    } catch (e: Exception) {
        e.printStackTrace()
        DEFAULT
    }

    private companion object {
        const val TAG = "AdsManager"
        const val DEFAULT = "en"
        const val RU = "ru"
        const val BE = "be"
    }
}