package com.katyrin.thundergram.utils

import android.Manifest.permission.CALL_PHONE
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.katyrin.thundergram.R
import com.katyrin.thundergram.view.notification.NotificationService
import java.io.File

fun Fragment.toast(message: String): Unit =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

fun Activity.toast(message: String): Unit =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Activity.toast(messageId: Int): Unit =
    Toast.makeText(this, messageId, Toast.LENGTH_LONG).show()

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

fun Activity.checkCallPermission(onPermissionGranted: () -> Unit): Unit = when {
    isCallPermissionGranted() -> onPermissionGranted()
    shouldShowRequestPermissionRationale(this, CALL_PHONE) -> showRationaleDialog()
    else -> requestCallPermission()
}

private fun Activity.isCallPermissionGranted(): Boolean = PackageManager.PERMISSION_GRANTED ==
        ContextCompat.checkSelfPermission(this, CALL_PHONE)

private fun Activity.showRationaleDialog(): Unit =
    AlertDialog.Builder(this)
        .setTitle(getString(R.string.access_to_call))
        .setMessage(getString(R.string.explanation_get_call))
        .setPositiveButton(getString(R.string.grant_access)) { _, _ -> requestCallPermission() }
        .setNegativeButton(getString(R.string.do_not)) { dialog, _ -> dialog.dismiss() }
        .create()
        .show()

fun Activity.requestCallPermission(): Unit =
    requestPermissions(this, arrayOf(CALL_PHONE), REQUEST_CALL_CODE)

fun Context.onStartService() {
    val intent = Intent(this, NotificationService::class.java)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        ContextCompat.startForegroundService(this, intent)
    else startService(intent)
}

fun Context.onStopService() {
    stopService(Intent(this, NotificationService::class.java))
}