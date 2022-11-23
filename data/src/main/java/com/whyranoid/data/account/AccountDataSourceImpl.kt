package com.whyranoid.data.account

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.whyranoid.data.account.AccountDataSourceImpl.PreferenceKeys.nickName
import com.whyranoid.data.account.AccountDataSourceImpl.PreferenceKeys.profileImgUri
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountDataSourceImpl @Inject constructor(
    private val dataStoreDb: DataStore<Preferences>
) : AccountDataSource {
    private object PreferenceKeys {
        val uid = stringPreferencesKey(UID_KEY)
        val email = stringPreferencesKey(EMAIL_KEY)
        val nickName = stringPreferencesKey(NICK_NAME_KEY)
        val profileImgUri = stringPreferencesKey(PROFILE_IMG_URI)
    }

    override fun getUserNickName() = dataStoreDb.data
        .map { preferences ->
            preferences[nickName] ?: EMPTY_STRING
        }

    override fun getUserProfileImgUri() = dataStoreDb.data
        .map { preferences ->
            preferences[profileImgUri] ?: EMPTY_STRING
        }

    companion object {
        private const val UID_KEY = "uid"
        private const val EMAIL_KEY = "email"
        private const val NICK_NAME_KEY = "nick_name"
        private const val PROFILE_IMG_URI = "profile_img_uri"
        private const val EMPTY_STRING = ""
    }
}
