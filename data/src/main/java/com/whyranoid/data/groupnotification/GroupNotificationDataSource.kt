package com.whyranoid.data.groupnotification

import com.google.firebase.firestore.FirebaseFirestore
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
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class GroupNotificationDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    @OptIn(FlowPreview::class)
    fun getGroupNotifications(groupId: String): Flow<List<GroupNotification>> {
        val startNotificationFlow = getGroupStartNotifications(groupId)
        val finishNotificationFlow = getGroupFinishNotifications(groupId)

        return flowOf(
            startNotificationFlow,
            finishNotificationFlow
        ).flattenMerge()
    }

    private fun getGroupStartNotifications(groupId: String): Flow<List<GroupNotification>> =
        callbackFlow {
            db.collection(GROUP_NOTIFICATIONS_COLLECTION)
                .document(groupId)
                .collection(START_NOTIFICATION)
                .addSnapshotListener { snapshot, error ->
                    val startNotificationList = mutableListOf<GroupNotification>()
                    snapshot?.forEach { document ->
                        val startNotification =
                            document.toObject(StartNotificationResponse::class.java)
                        startNotificationList.add(startNotification.toStartNotification())
                    }
                    trySend(startNotificationList)
                }

            awaitClose()
        }

    suspend fun notifyRunningStart(uid: String, groupIdList: List<String>) {
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

    private fun getGroupFinishNotifications(groupId: String): Flow<List<GroupNotification>> =
        callbackFlow {
            db.collection(GROUP_NOTIFICATIONS_COLLECTION)
                .document(groupId)
                .collection(FINISH_NOTIFICATION)
                .addSnapshotListener { snapshot, error ->
                    val finishNotificationList = mutableListOf<GroupNotification>()
                    snapshot?.forEach { document ->
                        val finishNotificationResponse =
                            document.toObject(FinishNotificationResponse::class.java)

                        finishNotificationResponse.let { finishNotificationResponse ->
                            db.collection(RUNNING_HISTORY_COLLECTION)
                                .document(finishNotificationResponse.historyId)
                                .get()
                                .addOnSuccessListener {
                                    val runningHistory = it.toObject(RunningHistory::class.java)

                                    runningHistory?.let {
                                        finishNotificationList.add(
                                            FinishNotification(
                                                uid = finishNotificationResponse.uid,
                                                runningHistory = runningHistory
                                            )
                                        )
                                    }
                                }
                        }
                    }
                    trySend(finishNotificationList)
                }

            awaitClose()
        }
}
