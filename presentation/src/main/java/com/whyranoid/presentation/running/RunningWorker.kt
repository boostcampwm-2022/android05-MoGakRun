package com.whyranoid.presentation.running

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.whyranoid.presentation.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class RunningWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val runningRepository: RunningRepository
) : CoroutineWorker(context, params) {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private lateinit var locationListener: LocationListener

    override suspend fun doWork(): Result {
        if (startTracking().not()) {
            runningRepository.finishRunning()
            return Result.failure()
        }

        setForeground(createForegroundInfo(context.getString(R.string.running_notification_content)))

        while ((runningRepository.runningState.value is RunningState.NotRunning).not()) {
            delay(1000)
            when (runningRepository.runningState.value) {
                is RunningState.NotRunning -> break
                is RunningState.Paused -> continue
                is RunningState.Running -> runningRepository.tick()
            }
        }
        locationManager.removeUpdates(locationListener)
        return Result.success()
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val id = context.getString(R.string.running_notification_id)
        val title = context.getString(R.string.running_notification_title)

        val intent = Intent(context, RunningActivity::class.java)
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
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val lastLatitude = location.latitude
                val lastLongitude = location.longitude

                runningRepository.setRunningState(
                    RunningPosition(lastLatitude, lastLongitude)
                )
            }

            override fun onProviderDisabled(provider: String) {
                runningRepository.pauseRunning()
            }
        }

        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                0f,
                locationListener,
                Looper.getMainLooper()
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                0f,
                locationListener,
                Looper.getMainLooper()
            )
            runningRepository.startRunning()
            return true
        } catch (e: SecurityException) {
            runningRepository.pauseRunning()
        } catch (e: Exception) {
            runningRepository.pauseRunning()
        }
        return false
    }

    companion object {
        const val WORKER_NAME = "runningWorker"
        const val NOTIFICATION_ID = 1000
        const val CHANNEL_ID = "모각런"
    }
}
