package com.university_project.jobly.baseviewmodel.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.datamodel.CompanyRQ

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
        if (fieldName == "CompanyRQ") {
            Log.d("TAG", "updateProfile: ${auth.uid.toString()}")
            val companyRQ = CompanyRQ(value, System.currentTimeMillis(), auth.uid.toString())
            Firebase.firestore.collection("CompanyRQ").document(auth.uid.toString()).set(companyRQ)
        } else {
            dbProfile.update(fieldName, value)
        }
    }
}