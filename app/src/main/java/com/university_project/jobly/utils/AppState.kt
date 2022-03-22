package com.university_project.jobly.utils

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner

class AppState:Application() {
    private val lifecycleListener: AppLifeCycleListner by lazy {
        AppLifeCycleListner()
    }

    override fun onCreate() {
        super.onCreate()
        setupLifecycleListener()
    }
    private fun setupLifecycleListener() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleListener)
    }
}