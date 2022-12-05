package com.whyranoid.presentation.running

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.whyranoid.presentation.MainActivity
import com.whyranoid.presentation.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class RunningWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val runningDataManager: RunningDataManager
) : CoroutineWorker(context, params) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_MS).build()
    private lateinit var locationCallback: LocationCallback

    override suspend fun doWork(): Result {
        if (startTracking().not()) {
            runningDataManager.finishRunning()
            return Result.failure()
        }

        setForeground(createForegroundInfo(context.getString(R.string.running_notification_content)))

        while ((runningDataManager.runningState.value is RunningState.NotRunning).not()) {
            delay(UPDATE_INTERVAL_MS)
            when (runningDataManager.runningState.value) {
                is RunningState.NotRunning -> break
                is RunningState.Paused -> continue
                is RunningState.Running -> runningDataManager.tick()
            }
        }
        fusedLocationClient.removeLocationUpdates(locationCallback)
        return Result.success()
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val id = context.getString(R.string.running_notification_id)
        val title = context.getString(R.string.running_notification_title)

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.kong)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ForegroundInfo(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
        }
        return ForegroundInfo(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = context.getString(R.string.running_channel_name)
        val descriptionText = context.getString(R.string.running_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    private fun startTracking(): Boolean {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    runningDataManager.setRunningState(location)
                } ?: run {
                    runningDataManager.pauseRunning()
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            runningDataManager.startRunning()
            return true
        } catch (e: SecurityException) {
            runningDataManager.pauseRunning()
        } catch (e: Exception) {
            runningDataManager.pauseRunning()
        }
        return false
    }

    companion object {
        const val WORKER_NAME = "runningWorker"
        const val NOTIFICATION_ID = 1000
        const val CHANNEL_ID = "모각런"
        const val UPDATE_INTERVAL_MS = 1000L
    }
}
