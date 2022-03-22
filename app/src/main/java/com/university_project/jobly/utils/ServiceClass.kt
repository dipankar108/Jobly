package com.university_project.jobly.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ServiceClass : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TAGService", "onStartCommand: started")
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAGService", "onDestroy: started")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("TAGService", "onTaskRemoved: started")
        stopSelf()
    }
}