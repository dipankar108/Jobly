package com.university_project.jobly.utils

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Disconnect {
    fun disconnect() {
        Firebase.firestore.collection("User").document(Firebase.auth.uid.toString()).update(
            "active", false
        ).addOnSuccessListener {
            Log.d("TAGM", "disconnect: Success")
        }
    }
}