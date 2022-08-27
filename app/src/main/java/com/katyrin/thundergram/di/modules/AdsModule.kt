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
        when (getDetectedCountry(context)) {
            RU, BE, UK -> AdManagerYandex()
            else -> AdManagerGoogle()
        }

    private fun getDetectedCountry(context: Context): String {
        context.detectNetworkCountry()?.let { return it }
        context.detectSIMCountry()?.let { return it }
        detectLocaleCountry()?.let { return it }
        return DEFAULT
    }

    private fun Context.detectSIMCountry(): String? = try {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        Log.d(TAG, "detectSIMCountry: ${telephonyManager.simCountryIso}")
        telephonyManager.simCountryIso
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private fun Context.detectNetworkCountry(): String? = try {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        Log.d(TAG, "detectNetworkCountry: ${telephonyManager.networkCountryIso}")
        telephonyManager.networkCountryIso
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private fun detectLocaleCountry(): String? = try {
        val localeCountryISO = Locale.getDefault().language
        Log.d(TAG, "detectLocaleCountry: $localeCountryISO")
        localeCountryISO
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private companion object {
        const val TAG = "AdsManager"
        const val DEFAULT = "en"
        const val RU = "ru"
        const val BE = "be"
        const val UK = "uk"
    }
}