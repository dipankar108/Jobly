package com.university_project.jobly.baseviewmodel.profile

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ProfileRep {
    val auth = Firebase.auth
    private var userInfo = MutableLiveData(false)
    val dbProfile = Firebase.firestore.collection("User").document(auth.uid.toString())
    fun getUserData(): MutableLiveData<Boolean> {
        dbProfile.addSnapshotListener { it, _ ->
            it?.let {
                userInfo.value = it.data?.get("banned") as Boolean
            }

        }
        return userInfo
    }

    fun updateProfile(value: String, fieldName: String) {
        dbProfile.update(fieldName, value)
    }
}