package com.whyranoid

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject

class SignInDataSource @Inject constructor(
    private val db: DataStore<Preferences>
)
