package com.whyranoid.data

import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

data class FirebaseGroup(
    val groupId: String = "",
    val groupName: String = "",
    val introduce: String = "",
    val leaderId: String = "",
    val membersId: List<String> = emptyList(),
    val rules: List<String> = emptyList()
)

// 그룹 생성하기
fun writeGroup() {
    db.collection("Groups")
        .document(groupId)
        .set(
            FirebaseGroup(
                groupId = groupId,
                groupName = "승민이의 그룹",
                introduce = "달려~ 달려~",
                leaderId = "승민 신",
                membersId = "병희 용한 현수".split(" "),
                rules = listOf(
                    "월-14-50",
                    "화-20-20"
                )
            )
        )
}

// 그룹 id로 그룹 정보 읽어오기
suspend fun readGroup(): FirebaseGroup {
    val groupData = db.collection("Groups")
        .document(groupId)
        .get()
        .await()

    return groupData.toObject(FirebaseGroup::class.java) ?: fakeGroup
}

// 그룹에 멤버 추가
fun joinUserToGroup() {
    db.collection("Groups")
        .document(groupId)
        .update(
            "membersId", FieldValue.arrayUnion("숙지")
        )
}

// 그룹에서 멤버 제거
fun exitUserToGroup() {
    db.collection("Groups")
        .document(groupId)
        .update(
            "membersId", FieldValue.arrayRemove("숙지")
        )
}

// 그룹에 규칙 추가
fun addRuleToGroup() {
    db.collection("Groups")
        .document(groupId)
        .update(
            "rules", FieldValue.arrayUnion("일-12-00")
        )
}

// 그룹에서 멤버 제거
fun removeRUleToGroup() {
    db.collection("Groups")
        .document(groupId)
        .update(
            "rules", FieldValue.arrayRemove("일-12-00")
        )
}