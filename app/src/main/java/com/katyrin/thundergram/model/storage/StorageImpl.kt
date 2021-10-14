package com.katyrin.thundergram.model.storage

import android.content.Context
import android.content.SharedPreferences
import com.katyrin.thundergram.R
import javax.inject.Inject

class StorageImpl @Inject constructor(
    private val context: Context
) : Storage {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    override fun getLogged(): Boolean = prefs.getBoolean(IS_LOGGED, false)

    override fun setLogged(isLogged: Boolean) {
        prefs.edit()
            .apply { putBoolean(IS_LOGGED, isLogged) }
            .apply()
    }

    private companion object {
        const val IS_LOGGED = "IS_LOGGED"
    }
}