package com.whyranoid.data.groupnotification

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.whyranoid.data.constant.CollectionId.FINISH_NOTIFICATION
import com.whyranoid.data.constant.CollectionId.GROUP_NOTIFICATIONS_COLLECTION
import com.whyranoid.data.constant.CollectionId.RUNNING_HISTORY_COLLECTION
import com.whyranoid.data.constant.CollectionId.START_NOTIFICATION
import com.whyranoid.data.model.FinishNotificationResponse
import com.whyranoid.data.model.StartNotificationResponse
import com.whyranoid.data.model.toStartNotification
import com.whyranoid.domain.model.FinishNotification
import com.whyranoid.domain.model.GroupNotification
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.model.StartNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class GroupNotificationDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : GroupNotificationDataSource {

    @OptIn(FlowPreview::class)
    override fun getGroupNotifications(groupId: String): Flow<List<GroupNotification>> {
        val startNotificationFlow = getGroupStartNotifications(groupId)
        val finishNotificationFlow = getGroupFinishNotifications(groupId)

        return flowOf(
            startNotificationFlow,
            finishNotificationFlow
        ).flattenMerge()
    }

    private fun getGroupStartNotifications(groupId: String): Flow<List<GroupNotification>> =
        callbackFlow {
            val registration = db.collection(GROUP_NOTIFICATIONS_COLLECTION)
                .document(groupId)
                .collection(START_NOTIFICATION)
                .addSnapshotListener { snapshot, _ ->
                    val startNotificationList = mutableListOf<GroupNotification>()
                    snapshot?.forEach { document ->
                        val startNotification =
                            document.toObject(StartNotificationResponse::class.java)
                        startNotificationList.add(startNotification.toStartNotification())
                    }
                    trySend(startNotificationList)
                }

            awaitClose {
                registration.remove()
            }
        }

    private fun getGroupFinishNotifications(groupId: String): Flow<List<GroupNotification>> {
        return db.collection(GROUP_NOTIFICATIONS_COLLECTION)
            .document(groupId)
            .collection(FINISH_NOTIFICATION)
            .snapshots()
            .map { snapshot ->
                val finishNotificationList = mutableListOf<GroupNotification>()

                snapshot.forEach { document ->
                    val finishNotificationResponse =
                        document.toObject(FinishNotificationResponse::class.java)

                    finishNotificationResponse.let { finishNotification ->
                        getRunningHistory(finishNotification.historyId)?.let { runningHistory ->
                            finishNotificationList.add(
                                FinishNotification(
                                    uid = finishNotification.uid,
                                    runningHistory = runningHistory
                                )
                            )
                        }
                    }
                }
                finishNotificationList
            }
    }

    // TODO : 예외처리
    private suspend fun getRunningHistory(historyId: String): RunningHistory? {
        return db.collection(RUNNING_HISTORY_COLLECTION)
            .document(historyId)
            .get()
            .await()
            .toObject(RunningHistory::class.java)
    }

    override suspend fun notifyRunningStart(uid: String, groupIdList: List<String>) {
        withContext(Dispatchers.IO) {
            groupIdList.forEach { groupId ->
                db.collection(GROUP_NOTIFICATIONS_COLLECTION)
                    .document(groupId)
                    .collection(START_NOTIFICATION)
                    .document(UUID.randomUUID().toString())
                    .set(
                        StartNotification(
                            startedAt = System.currentTimeMillis(),
                            uid = uid
                        )
                    )
            }
        }
    }

    override suspend fun notifyRunningFinish(
        uid: String,
        runningHistory: RunningHistory,
        groupIdList: List<String>
    ) {
        withContext(Dispatchers.IO) {
            groupIdList.forEach { groupId ->
                db.collection(GROUP_NOTIFICATIONS_COLLECTION)
                    .document(groupId)
                    .collection(FINISH_NOTIFICATION)
                    .document(UUID.randomUUID().toString())
                    .set(
                        FinishNotificationResponse(
                            uid = uid,
                            historyId = runningHistory.historyId
                        )
                    )
            }
        }
    }
}
