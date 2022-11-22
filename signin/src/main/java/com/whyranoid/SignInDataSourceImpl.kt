package com.whyranoid

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject

class SignInDataSourceImpl @Inject constructor(
    private val dataStoreDb: DataStore<Preferences>
) : SignInDataSource {

    private object PreferenceKeys {
        val uid = stringPreferencesKey(UID_KEY)
        val email = stringPreferencesKey(EMAIL_KEY)
        val nickName = stringPreferencesKey(NICK_NAME_KEY)
        val profileImgUrl = stringPreferencesKey(PROFILE_IMG_URI)
    }

    override suspend fun saveLogInUserInfo(userInfo: SignInUserInfo): Boolean {
        dataStoreDb.edit { preferences ->
            preferences[PreferenceKeys.uid] = userInfo.uid
            preferences[PreferenceKeys.email] = userInfo.email ?: EMPTY_STRING
            preferences[PreferenceKeys.nickName] = userInfo.nickName ?: EMPTY_STRING
            preferences[PreferenceKeys.profileImgUrl] = userInfo.profileImgUri ?: EMPTY_STRING
        }
        return true
    }

    companion object {
        private const val UID_KEY = "uid"
        private const val EMAIL_KEY = "email"
        private const val NICK_NAME_KEY = "nick_name"
        private const val PROFILE_IMG_URI = "profile_img_uri"
        private const val EMPTY_STRING = ""
    }
}
