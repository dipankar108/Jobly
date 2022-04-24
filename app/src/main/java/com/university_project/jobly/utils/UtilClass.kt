package com.university_project.jobly.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.accountlog.AccountLog
import com.university_project.jobly.baseviewmodel.Repository

object UtilClass {
    fun signOutNow(context: Context, activity: Activity, sh: SharedPreferences.Editor) {

        Repository.updateActiveStatus(false)
        Firebase.auth.signOut()
        sh.clear().apply()
        val intent = Intent(context, AccountLog::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
        activity.finish()
    }
}