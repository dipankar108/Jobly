package com.university_project.jobly.baseviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ProfileRep {
    val auth=Firebase.auth
     private  var userInfo=MutableLiveData<Boolean>()
    fun getUserData():MutableLiveData<Boolean>{
        
        val db=Firebase.firestore.collection("User").document(auth.uid.toString()).get()
        db.addOnSuccessListener {
            userInfo.value= it.data!!["banned"] as Boolean?
        }
        return userInfo
    }
}