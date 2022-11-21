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
        val nickName = stringPreferencesKey(NICK_NAME_KEY)
        val profileImgUrl = stringPreferencesKey(PROFILE_IMG_URI)
    }

    override suspend fun saveLogInUserInfo(userInfo: User): Boolean {
        dataStoreDb.edit { preferences ->
            preferences[PreferenceKeys.uid] = userInfo.uid
            preferences[PreferenceKeys.nickName] = userInfo.nickName ?: ""
            preferences[PreferenceKeys.profileImgUrl] = userInfo.profileImgUri ?: ""
        }
        return true
    }

    companion object {
        private const val UID_KEY = "uid"
        private const val NICK_NAME_KEY = "nick_name"
        private const val PROFILE_IMG_URI = "profile_img_uri"
    }
}
