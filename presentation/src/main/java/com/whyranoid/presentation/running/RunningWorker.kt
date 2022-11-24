package com.whyranoid.presentation.running

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltWorker
class RunningWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val runningRepository: RunningRepository
) : CoroutineWorker(context, params) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    override suspend fun doWork(): Result {
        startTracking()

        while ((runningRepository.runningState.value is RunningState.NotRunning).not()) {
            delay(1000)
            when (runningRepository.runningState.value) {
                is RunningState.NotRunning -> break
                is RunningState.Paused -> continue
                is RunningState.Running -> trackRunningData()
            }
        }
        return Result.success()
    }

    private suspend fun startTracking(): Boolean {
        // TODO: getCurrentLocation을 requestLocationUpdates로 변경
        return suspendCoroutine { continuation ->
            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                            return CancellationTokenSource().token
                        }

                        override fun isCancellationRequested(): Boolean {
                            return false
                        }
                    }
                ).addOnSuccessListener { location ->
                    runningRepository.startRunning(
                        RunningPosition(
                            location.latitude,
                            location.longitude
                        )
                    )
                    continuation.resume(true)
                }.addOnFailureListener {
                    runningRepository.pauseRunning()
                    continuation.resume(false)
                }
            } catch (exception: SecurityException) {
                runningRepository.pauseRunning()
                continuation.resume(false)
            } catch (e: Exception) {
                continuation.resume(false)
            }
        }
    }

    private fun trackRunningData() {
        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                        return CancellationTokenSource().token
                    }

                    override fun isCancellationRequested(): Boolean {
                        return false
                    }
                }
            ).addOnSuccessListener { location ->
                val lastLatitude = location.latitude
                val lastLongitude = location.longitude

                runningRepository.setRunningState(
                    RunningPosition(lastLatitude, lastLongitude)
                )
            }.addOnFailureListener {
                runningRepository.pauseRunning()
            }
        } catch (exception: SecurityException) {
            runningRepository.pauseRunning()
        } catch (e: Exception) {
            runningRepository.pauseRunning()
        }
    }

    companion object {
        const val WORKER_NAME = "runningWorker"
    }
}
