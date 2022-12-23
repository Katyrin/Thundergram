package com.katyrin.thundergram.view.notification

import android.app.Notification
import android.app.Notification.VISIBILITY_SECRET
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.*
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.katyrin.thundergram.R
import javax.inject.Inject

class NotificationGeneratorImpl @Inject constructor(
    private val context: Context
) : NotificationGenerator {

    override fun getForegroundNotification(): Notification = with(context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) setNotificationForegroundServiceChannel()
        NotificationCompat.Builder(this, CHANNEL_SERVICE_ID).apply {
            setSmallIcon(R.drawable.ic_send)
            setContentTitle(context.getString(R.string.service_title))
            setContentText(context.getString(R.string.service_message))
            priority = NotificationCompat.PRIORITY_MIN
            setContentIntent(getPendingIntentOpenApp())
        }.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setNotificationForegroundServiceChannel(): Unit = with(context) {
        val channel = NotificationChannel(CHANNEL_SERVICE_ID, CHANNEL_NAME, IMPORTANCE_MIN)
        channel.lockscreenVisibility = VISIBILITY_SECRET
        channel.importance = IMPORTANCE_MIN
        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

    private fun getPendingIntentOpenApp(): PendingIntent =
        NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.chatListFragment)
            .createPendingIntent()

    override fun showMessageNotification(
        userId: Long,
        chatId: Long,
        chatName: String,
        title: String,
        message: String,
        userPhotoPath: String?
    ) {
        val bundle = Bundle()
        bundle.putLong(CHAT_ID, chatId)
        bundle.putString(CHAT_NAME, chatName)
        val channelId: String = CHANNEL_MESSAGE_ID + chatId
        val messageText = if (title == chatName) message else "$title: $message"
        val notificationBuilder =
            createNotificationBuilder(bundle, chatName, messageText, userPhotoPath, channelId)
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(channelId, chatName)
        notificationManager.notify(chatId.toInt(), notificationBuilder.build())
    }

    private fun createNotificationBuilder(
        bundle: Bundle,
        title: String,
        message: String,
        userPhotoPath: String?,
        channelId: String
    ): NotificationCompat.Builder = NotificationCompat.Builder(context, channelId).apply {
        setSmallIcon(R.drawable.ic_send)
        setLargeIcon(BitmapFactory.decodeFile(userPhotoPath))
        setContentTitle(title)
        setContentText(message)
        setStyle(NotificationCompat.BigTextStyle().bigText(message))
        priority = NotificationCompat.PRIORITY_MAX
        setSound(Uri.parse(getSoundString()))
        setVibrate(getVibrationPattern())
        setContentIntent(getPendingIntent(bundle))
        setAutoCancel(true)
    }

    private fun getSoundString(): String =
        ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + SOUND_PATH

    private fun getPendingIntent(bundle: Bundle): PendingIntent =
        NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.chatFragment)
            .setArguments(bundle)
            .createPendingIntent()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun NotificationManager.createNotificationChannel(channelId: String, chatName: String) {
        val attributes: AudioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        val channel = NotificationChannel(channelId, chatName, IMPORTANCE_HIGH)
        channel.setSound(Uri.parse(getSoundString()), attributes)
        channel.vibrationPattern = getVibrationPattern()
        createNotificationChannel(channel)
    }

    private fun getVibrationPattern(): LongArray =
        longArrayOf(0, 300, 200, 900, 50, 75, 50, 150, 50, 75, 50, 75, 50, 300)

    private companion object {
        const val CHANNEL_SERVICE_ID = "com.katyrin.thundergram.run_service"
        const val CHANNEL_NAME = "Foreground service"
        const val CHANNEL_MESSAGE_ID = "com.katyrin.thundergram.notification."
        const val CHAT_ID = "chatId"
        const val CHAT_NAME = "chatName"
        const val SOUND_PATH = "/raw/thunder_storm"
    }
}