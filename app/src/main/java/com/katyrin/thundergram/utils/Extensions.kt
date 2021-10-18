package com.katyrin.thundergram.utils

import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.katyrin.thundergram.R
import java.io.File

fun Fragment.toast(message: String): Unit =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

fun ImageView.setChatIconFromUri(uri: String?, placeholder: Int = R.drawable.ic_user_no_photo) {
    Glide.with(context)
        .load(if (uri.isNullOrEmpty()) placeholder else File(uri))
        .placeholder(placeholder)
        .error(R.drawable.ic_user_no_photo)
        .circleCrop()
        .into(this)
}