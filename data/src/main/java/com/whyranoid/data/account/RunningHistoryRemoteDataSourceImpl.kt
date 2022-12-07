package com.whyranoid.data.account

import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.constant.CollectionId
import com.whyranoid.data.model.toRunningHistoryResponse
import com.whyranoid.domain.model.RunningHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RunningHistoryRemoteDataSourceImpl @Inject constructor(private val firebaseDB: FirebaseFirestore) :
    RunningHistoryRemoteDataSource {
    override suspend fun uploadRunningHistory(uid: String, runningHistory: RunningHistory): Result<Boolean> {
        val runningHistoryResponse = runningHistory.toRunningHistoryResponse(uid)
        var uploadSuccess = false

        return runCatching {
            withContext(Dispatchers.IO) {
                val task = firebaseDB.collection(CollectionId.RUNNING_HISTORY_COLLECTION)
                    .document(runningHistoryResponse.historyId)
                    .set(runningHistoryResponse)
                    .addOnSuccessListener {
                        uploadSuccess = true
                    }
                    .addOnFailureListener { exception ->
                        throw exception
                    }

                task.await()
            }
            uploadSuccess
        }
    }
}
