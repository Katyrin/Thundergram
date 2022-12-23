package com.katyrin.thundergram.view.notification.worker

import android.content.Context
import androidx.work.*
import androidx.work.NetworkType.CONNECTED
import androidx.work.WorkInfo.State.RUNNING
import androidx.work.WorkInfo.State.ENQUEUED
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationWorkerGeneratorImpl @Inject constructor(
    private val context: Context
) : NotificationWorkerGenerator {

    override fun startNotificationWork() {
        if (isWorkScheduled()) WorkManager.getInstance(context).cancelAllWorkByTag(TAG_NOTIFY_WORK)
        val keep = ExistingPeriodicWorkPolicy.KEEP
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(TAG_NOTIFY_WORK, keep, getPeriodicWorkRequest())
    }

    private fun getPeriodicWorkRequest(): PeriodicWorkRequest =
        Constraints.Builder().setRequiredNetworkType(CONNECTED).build().let { constraints ->
            PeriodicWorkRequest.Builder(
                NotificationWorker::class.java,
                REPEAT_INTERVAL, TimeUnit.MINUTES,
                FIX_INTERVAL, TimeUnit.MINUTES
            )
                .addTag(TAG_NOTIFY_WORK)
                .setConstraints(constraints)
                .build()
        }

    private fun isWorkScheduled(): Boolean = try {
        var running = false
        WorkManager.getInstance(context).getWorkInfosByTag(TAG_NOTIFY_WORK).get()
            .forEach { running = it.state == RUNNING || it.state == ENQUEUED }
        running
    } catch (e: ExecutionException) {
        e.printStackTrace()
        false
    } catch (e: InterruptedException) {
        e.printStackTrace()
        false
    }

    private companion object {
        const val TAG_NOTIFY_WORK = "TAG_NOTIFY_WORK"
        const val FIX_INTERVAL = 25L
        const val REPEAT_INTERVAL = 30L
    }
}