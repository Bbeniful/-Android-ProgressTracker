package com.bbeniful.progresstrackergym

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.plugin.module.dsl.startKoin

@KoinApplication(modules = [AppModule::class])
class ProgressTrackerGYMApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin<ProgressTrackerGYMApp> {
            androidContext(this@ProgressTrackerGYMApp)
            androidLogger()
        }
    }
}