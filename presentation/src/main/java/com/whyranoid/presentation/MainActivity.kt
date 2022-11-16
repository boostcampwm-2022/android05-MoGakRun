package com.whyranoid.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.whyranoid.data.addRuleToGroup
import com.whyranoid.data.collectEndGroupNotifications
import com.whyranoid.data.collectStartNotificationTest
import com.whyranoid.data.deleteRunner
import com.whyranoid.data.exitUserToGroup
import com.whyranoid.data.getRunnerCount
import com.whyranoid.data.joinUserToGroup
import com.whyranoid.data.readGroup
import com.whyranoid.data.removeRUleToGroup
import com.whyranoid.data.writeEndGroupNotifications
import com.whyranoid.data.writeGroup
import com.whyranoid.data.writeRunner
import com.whyranoid.data.writeStartGroupNotifications
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import readUser
import userExitGroup
import userJoinGroup
import writeUser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TODO("테스트해볼 메서드를 제외하고 주석처리해서 테스트하면 됩니다.")
        // 유저 가입하기
        writeUser()

        // uid로 유저 읽기
        lifecycleScope.launch {
            readUser().apply {
                println("유저 $this")
            }
        }

        // 유저가 그룹 가입하기
        userJoinGroup()
        // 유저가 그룹 탈퇴하기
        userExitGroup()

        // 그룹 생성하기
        writeGroup()
        // 그룹 읽기
        lifecycleScope.launch {
            println(readGroup())
        }

        // 그룹에 멤버 추가
        joinUserToGroup()
        // 그룹에 멤버 제거
        exitUserToGroup()

        // 그룹에 룰 추가
        addRuleToGroup()

        // 그룹에 룰 제거
        removeRUleToGroup()

        // 달리기 시작(러닝 등록)
        writeRunner()
        // 달리기 종료(러닝 해제)
        deleteRunner()

        // 러너 숫자 감지
        lifecycleScope.launch {
            getRunnerCount().collect {
                println("러너 등장 $it")
            }
        }

        // 운동 시작 알림 보내기
        writeStartGroupNotifications()

        // 운동 종료 알림 보내기
        writeEndGroupNotifications()

        // 내 그룹 시작 알림 감지하기
        lifecycleScope.launch {
            collectStartNotificationTest().collect {
                println("감지 $it")
            }
        }

        // 내 그룹 종료 알림 감지하기
        lifecycleScope.launch {
            collectEndGroupNotifications().collect {
                println("감지 $it")
            }
        }
    }
}
