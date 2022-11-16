package com.whyranoid.data

import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

enum class NotificationType(val type: String) {
    START("start"),
    END("end")
}

fun readOnlyStartNotifications() {
    db.collection("StartNotifications")
        .document("test")
        .addSnapshotListener { snapshot, e ->
            val startNotification = snapshot?.toObject(StartNotification::class.java)
            println("시작 알림 $startNotification")
        }
}

fun readOnlyFinishNotifications() {
    db.collection("FinishNotifications")
        .document("test")
        .addSnapshotListener { snapshot, e ->

            db.collection("FinishNotifications")
                .document("test")
                .get()
                .addOnCompleteListener { snapshot ->
                    val document = snapshot.result
                    println("종료 알림 ${document.get("test")}")
                    val finishNotification = document?.toObject(Notifications::class.java)
                    println("종료 알림 $finishNotification")
                }
        }
}

fun writeStartGroupNotifications() {
    db.collection("GroupNotifications")
        .document("collections")
        .collection(NotificationType.START.type)
        .document(UUID.randomUUID().toString())
        .set(
            StartNotification(
                startedAt = System.currentTimeMillis(),
                type = NotificationType.START.type,
                uid = "hsjeon"
            )
        )
}

fun collectStartNotificationTest() {
    db.collection("GroupNotifications")
        .document("collections")
        .collection("start")
        .addSnapshotListener { snapshot, error ->
            snapshot?.forEach { document ->
                document.toObject(StartNotification::class.java).apply {
                    println("알림 시작 노티$this")
                }
            }
        }
}

fun writeEndGroupNotifications() {
    db.collection("GroupNotifications")
        .document("collections")
        .collection(NotificationType.END.type)
        .document(UUID.randomUUID().toString())
        .set(
            FirebaseEndNotification(
                finishedAt = 5435324L,
                historyId = "123123",
                pace = 21.0,
                startedAt = 4384134234L,
                totalRunningTime = 3610,
                totalDistance = 41.2,
                type = NotificationType.END.type,
                uid = "asdoifasdpof"
            )
        )
}

fun collectEndGroupNotifications(): Flow<EndNotification> = callbackFlow {
    db.collection("GroupNotifications")
        .document("collections")
        .collection("end")
        .addSnapshotListener { snapshot, error ->
            snapshot?.forEach { document ->
                println("알림 ${document.data.values}")
                val data = document.toObject(FirebaseEndNotification::class.java)
                println("알림 $data")
                val paredData = data.toEndNotification()
                println("알림 $paredData")
                trySend(paredData)
            }
        }

    awaitClose()
}

// fun arrayAddStartNotificationTest() {
//    db.collection("FinishNotifications")
//
// }

fun arrayAddEndNotificationTest() {
    db.collection("FinishNotifications")
        .document("test")
        .update(
            "notifications",
            FieldValue.arrayUnion(
                EndNotification(
                    runningHistory = RunningHistory(
                        finishedAt = 123123123L,
                        historyId = "123123",
                        pace = 21.0,
                        startedAt = 4384134234L,
                        totalRunningTime = 3610,
                        totalDistance = 41.2
                    ),
                    type = "end",
                    uid = "asdoifasdpof"
                )
            )
        )
}

data class Notifications(
    var notificationList: MutableList<EndNotification> = mutableListOf()
)

sealed interface Notification {
    val type: String
    val uid: String
}

data class StartNotification(
    val startedAt: Long = 0L,
    override val uid: String = "",
    override val type: String = ""
) : Notification

data class EndNotification(
    override val uid: String = "",
    override val type: String = "",
    var runningHistory: RunningHistory =
        RunningHistory(
            0L,
            "",
            0.0,
            0L,
            0.0,
            0
        )
) : Notification

data class RunningHistory(
    val finishedAt: Long,
    val historyId: String,
    val pace: Double,
    val startedAt: Long,
    val totalDistance: Double,
    val totalRunningTime: Int
)

// 파이어베이스에서 모든 필드를 다른 변수로 지정하기 위함
data class FirebaseEndNotification(
    override val type: String = "",
    override val uid: String = "",
    val finishedAt: Long = 0L,
    val historyId: String = "",
    val pace: Double = 0.0,
    val startedAt: Long = 0L,
    val totalDistance: Double = 0.0,
    val totalRunningTime: Int = 0
) : Notification

fun FirebaseEndNotification.toEndNotification(): EndNotification {
    return EndNotification(
        runningHistory = RunningHistory(
            this.finishedAt,
            this.historyId,
            this.pace,
            this.startedAt,
            this.totalDistance,
            this.totalRunningTime
        ),
        type = this.type,
        uid = this.uid
    )
}
