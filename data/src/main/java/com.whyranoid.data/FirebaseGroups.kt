package com.whyranoid.data

import java.util.*

val randomGroupId = UUID.randomUUID()

fun writeGroup() {

    // 그룹 아이디 테스트용 UUID - 랜덤(생성)하고 싶을 때 사용
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

fun readGroup() {
    db.collection("Groups")
        .document(groupId)
        .get()
        .addOnSuccessListener { snapshot ->

            // null도 그냥 들어옴. 예외처리 필요.
            println("그룹읽기 성공")
            snapshot.data.apply {
                println("그룹읽기 $this")
            }
        }
        .addOnFailureListener {
            println("그룹읽기 실패")
        }
}

data class FirebaseGroup(
    val groupId: String,
    val groupName: String,
    val introduce: String,
    val leaderId: String,
    val membersId: List<String>,
    val rules: List<String>
)
