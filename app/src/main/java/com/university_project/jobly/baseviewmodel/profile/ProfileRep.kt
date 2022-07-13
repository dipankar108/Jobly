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
    private val chatServer = Firebase.firestore.collection("ChatServer")
    fun getUserData(): MutableLiveData<Boolean> {
        dbProfile.addSnapshotListener { it, _ ->
            it?.let {
                userInfo.value = it.data?.get("banned") as Boolean
            }

        }
        return userInfo
    }

    fun updateChatImage(url: String,fieldName: String,updateFieldName:String) {
        chatServer.whereEqualTo(fieldName, auth.uid.toString()).get()
            .addOnSuccessListener { documents ->
                for (documentId in documents) {
                    chatServer.document(documentId.id).update(updateFieldName, url).addOnCompleteListener {
                        Log.d("TAGM", "updateChatImage: Updated")
                    }.addOnFailureListener { e ->
                        Log.d("TAGM", "uploadProfilePicToFirestore: ${e.message}")
                    }
                }
            }
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