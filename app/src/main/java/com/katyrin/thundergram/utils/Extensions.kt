package com.katyrin.thundergram.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message: String): Unit =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()