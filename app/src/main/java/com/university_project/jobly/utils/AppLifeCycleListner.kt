package com.university_project.jobly.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.university_project.jobly.baseviewmodel.Repository

class AppLifeCycleListner : DefaultLifecycleObserver {
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Repository.updateActiveStatus(false)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Repository.updateActiveStatus(true)
    }
}