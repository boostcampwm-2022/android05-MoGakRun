package com.whyranoid.data

import FirebaseUser

val fakeUser = FirebaseUser(
    "페이크",
    "페이크 유알엘",
    "아이디",
    "집에 가고 싶습니다.".split(" ")
)

val fakeGroup = FirebaseGroup(
    "val groupId: String,",
    "val groupName: String",
    "val introduce: String",
    "val leaderId: String",
    listOf("멤버", "하하", "uid"),
    "규칙이 담기게 될것임".split(" ")
)
