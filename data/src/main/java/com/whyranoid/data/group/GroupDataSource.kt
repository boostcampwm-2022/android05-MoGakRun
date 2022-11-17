package com.whyranoid.data.group

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.constant.CollectionId.GROUPS_COLLECTION
import com.whyranoid.data.constant.CollectionId.USERS_COLLECTION
import com.whyranoid.data.constant.FieldId.GROUP_INTRODUCE
import com.whyranoid.data.constant.FieldId.GROUP_MEMBERS_ID
import com.whyranoid.data.constant.FieldId.GROUP_NAME
import com.whyranoid.data.constant.FieldId.JOINED_GROUP_LIST
import com.whyranoid.data.constant.FieldId.RULES
import com.whyranoid.domain.model.Rule
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GroupDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun updateGroupInfo(
        groupId: String,
        groupName: String,
        groupIntroduce: String,
        rules: List<Rule>
    ): Boolean {
        return suspendCoroutine { continuation ->
            db.collection(GROUPS_COLLECTION)
                .document(groupId)
                .update(
                    mapOf(
                        GROUP_NAME to groupName,
                        GROUP_INTRODUCE to groupIntroduce,
                        RULES to rules.map { rule ->
                            rule.toString()
                        }
                    )
                )
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }

    suspend fun joinGroup(uid: String, groupId: String): Boolean {
        return suspendCoroutine<Boolean> { continuation ->
            db.collection(GROUPS_COLLECTION)
                .document(groupId)
                .update(
                    GROUP_MEMBERS_ID,
                    FieldValue.arrayUnion(uid)
                ).addOnSuccessListener {
                    continuation.resume(true)
                }.addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }

    suspend fun exitGroup(uid: String, groupId: String): Boolean {
        return suspendCoroutine { continuation ->
            db.collection(GROUPS_COLLECTION)
                .document(groupId)
                .update(
                    mapOf(
                        GROUP_MEMBERS_ID to FieldValue.arrayRemove(uid)
                    )
                ).addOnSuccessListener {
                    db.collection(USERS_COLLECTION)
                        .document(uid)
                        .update(
                            mapOf(
                                JOINED_GROUP_LIST to FieldValue.arrayRemove(groupId)
                            )
                        )
                        .addOnSuccessListener {
                            continuation.resume(true)
                        }
                        .addOnFailureListener {
                            continuation.resume(false)
                        }
                }.addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }
}
