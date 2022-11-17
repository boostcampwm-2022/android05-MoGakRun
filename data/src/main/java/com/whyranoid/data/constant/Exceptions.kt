package com.whyranoid.data.constant

object Exceptions {

    val TEMP_EXCEPTION = Exception("일시적인 오류가 발생하였습니다.")
    val NO_GROUP_EXCEPTION = Exception("해당하는 그룹이 없습니다.")
    val NO_USER_EXCEPTION = Exception("해당하는 유저가 없습니다.")
    val NO_JOINED_GROUP_EXCEPTION = Exception("가입한 그룹이 없습니다.")
}
