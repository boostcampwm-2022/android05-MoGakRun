package com.whyranoid.data.running

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.constant.CollectionId
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RunnerDataSourceImpl(private val db: FirebaseFirestore) : RunnerDataSource {

    override fun getCurrentRunnerCount(): Flow<Int> = callbackFlow {
        db.collection(CollectionId.RUNNERS_COLLECTION)
            .document("runnersId")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    val count = it.data?.size ?: -1
                    trySend(count)
                }
            }

        awaitClose()
    }

    override suspend fun startRunning(uid: String): Boolean {
        if (uid.isBlank()) return false
        return suspendCoroutine { continuation ->
            db.collection(CollectionId.RUNNERS_COLLECTION)
                .document("runnersId")
                .update(uid, uid)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }

    override suspend fun finishRunning(uid: String): Boolean {
        if (uid.isBlank()) return false
        return suspendCoroutine { continuation ->
            db.collection(CollectionId.RUNNERS_COLLECTION)
                .document("runnersId")
                .update(uid, FieldValue.delete())
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }
}
