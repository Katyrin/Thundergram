package com.katyrin.thundergram.view.notification.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.katyrin.thundergram.utils.onStartService
import javax.inject.Inject

class NotificationWorker @Inject constructor(
    private val context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result = try {
        context.onStartService()
        Result.success()
    } catch (e: Exception) {
        Result.failure()
    }
}