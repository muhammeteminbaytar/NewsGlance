package com.mbaytar.newsglance.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mbaytar.newsglance.R
import com.mbaytar.newsglance.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NewsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val notificationHelper = NotificationHelper(context)

    override suspend fun doWork(): Result {

        val dreamId = inputData.getString("notification_title")

        return if (dreamId?.isNotEmpty() == true) {
            notificationHelper.sendNotification(
                title = dreamId,
                messageBody = "",
                image = R.drawable.logo
            )
            Result.success()
        } else {
            Result.failure()
        }
    }
}