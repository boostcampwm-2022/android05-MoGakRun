package com.whyranoid.data.account

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.account.AccountDataSourceImpl.PreferenceKeys.nickName
import com.whyranoid.data.account.AccountDataSourceImpl.PreferenceKeys.profileImgUri
import com.whyranoid.data.account.AccountDataSourceImpl.PreferenceKeys.uid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AccountDataSourceImpl @Inject constructor(
    private val dataStoreDb: DataStore<Preferences>,
    private val fireBaseDb: FirebaseFirestore
) : AccountDataSource {

    init {
        getUid()
    }

    private var userUid = EMPTY_STRING

    private object PreferenceKeys {
        val uid = stringPreferencesKey(UID_KEY)
        val email = stringPreferencesKey(EMAIL_KEY)
        val nickName = stringPreferencesKey(NICK_NAME_KEY)
        val profileImgUri = stringPreferencesKey(PROFILE_IMG_URI)
    }

    // TODO : flow -> suspend로 변경해야 함.
    override fun getUserNickName() = dataStoreDb.data
        .map { preferences ->
            preferences[nickName] ?: EMPTY_STRING
        }

    // TODO : flow -> suspend로 변경해야 함.
    override fun getUserProfileImgUri() = dataStoreDb.data
        .map { preferences ->
            preferences[profileImgUri] ?: EMPTY_STRING
        }

    // TODO : flow -> suspend로 변경해야 함.
    private fun getUid() = dataStoreDb.data
        .map { preferences ->
            preferences[uid]
        }.onEach {
            userUid = it ?: EMPTY_STRING
        }.launchIn(CoroutineScope(Dispatchers.IO))

    override suspend fun updateUserNickName(newNickName: String) = runCatching {
        // 로컬에 업데이트
        dataStoreDb.edit { preferences ->
            preferences[nickName] = newNickName
        }

        // 리모트에 업데이트
        fireBaseDb.collection(USER_COLLECTION_PATH)
            .document(userUid).update(USER_FIELD_NICK_NAME, newNickName)

        newNickName
    }

    companion object {
        private const val UID_KEY = "uid"
        private const val EMAIL_KEY = "email"
        private const val NICK_NAME_KEY = "nick_name"
        private const val PROFILE_IMG_URI = "profile_img_uri"
        private const val EMPTY_STRING = ""
        private const val USER_COLLECTION_PATH = "Users"
        private const val USER_FIELD_NICK_NAME = "name"
    }
}
