package com.katyrin.thundergram.utils

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.katyrin.thundergram.R
import java.io.File

fun Fragment.toast(message: String): Unit =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

fun Activity.toast(message: String): Unit =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun ImageView.setChatIconFromUri(uri: String?, placeholder: Int = R.drawable.ic_user_no_photo) {
    Glide.with(context)
        .load(if (uri.isNullOrEmpty()) placeholder else File(uri))
        .placeholder(placeholder)
        .error(R.drawable.ic_user_no_photo)
        .circleCrop()
        .into(this)
}

fun ImageView.getContentPhotoFromUri(uri: String?, placeholder: Int = R.drawable.ic_user_no_photo) {
    Glide.with(context)
        .load(if (uri.isNullOrEmpty()) placeholder else File(uri))
        .placeholder(placeholder)
        .error(R.drawable.ic_user_no_photo)
        .into(this)
}

fun Fragment.hideKeyboard() {
    view?.let {
        val inputMethodManager =
            activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}