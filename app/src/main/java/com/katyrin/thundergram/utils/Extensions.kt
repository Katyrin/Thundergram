package com.katyrin.thundergram.utils

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.katyrin.thundergram.R
import com.katyrin.thundergram.view.notification.NotificationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

fun Context.toast(messageId: Int): Unit =
    Toast.makeText(this, messageId, Toast.LENGTH_LONG).show()

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

private fun Activity.isCallPermissionGranted(): Boolean =
    PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, CALL_PHONE)

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

fun AppCompatActivity.checkNotification(view: View, grantedCallback: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) when {
        ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PERMISSION_GRANTED ->
            grantedCallback()
        shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) -> showNoPermissionSnackBar(view)
        else -> requestPermissionLauncher(grantedCallback).launch(POST_NOTIFICATIONS)
    } else grantedCallback()
}

private fun Activity.showNoPermissionSnackBar(view: View) {
    Snackbar.make(view, getString(R.string.notification_disabled), Snackbar.LENGTH_LONG)
        .setAction(getString(R.string.settings_text)) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.fromParts(PACKAGE, packageName, null)
            startActivity(intent)
        }.show()
}

private fun AppCompatActivity.requestPermissionLauncher(
    grantedCallback: () -> Unit
): ActivityResultLauncher<String> =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) grantedCallback()
    }

fun Float.setNewSpeed(): Float = when (this) {
    FIRST_SPEED -> SECOND_SPEED
    SECOND_SPEED -> THIRD_SPEED
    else -> FIRST_SPEED
}

fun TextView.setTextRate(speed: Float): Unit = with(context) {
    text = when (speed) {
        FIRST_SPEED -> getString(R.string.first_speed)
        SECOND_SPEED -> getString(R.string.second_speed)
        else -> getString(R.string.third_speed)
    }
}

inline fun <T> MutableStateFlow<T>.update(function: T.() -> T) {
    update { function(value) }
}