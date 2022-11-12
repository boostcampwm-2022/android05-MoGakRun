package com.whyranoid.mogakrun

import android.app.Application
import com.whyranoid.mogakrun.util.TimberDebugTree
import timber.log.Timber

class MogakrunApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        }
    }
}
