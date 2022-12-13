package com.whyranoid.domain.model

class FakeGroupInfo private constructor() {

    companion object {
        private var INSTANCE: GroupInfo? = null

        fun getInstance() =
            INSTANCE ?: GroupInfo(
                name = "fake",
                groupId = "fake",
                introduce = "fake",
                rules = emptyList(),
                headCount = 0,
                leader = User(
                    uid = "fake",
                    name = "hyunsoo",
                    profileUrl = "fake"
                )
            ).apply {
                INSTANCE = this
            }
    }
}
