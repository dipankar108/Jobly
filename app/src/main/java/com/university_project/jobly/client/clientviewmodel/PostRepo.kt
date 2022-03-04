package com.university_project.jobly.client.clientviewmodel

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object PostRepo {
    val auth = Firebase.auth
    fun getResponseUsingLiveData(): MutableLiveData<List<ClientPostDataModel>> {
        val mutableLiveData = MutableLiveData<List<ClientPostDataModel>>()
        val db = Firebase.firestore.collection("JobPost")
        val query = db.whereEqualTo("userId", auth.uid.toString())

        query.get().addOnSuccessListener {
            val myPost= ArrayList<ClientPostDataModel>()
            for (doc in it.documents) {
                val singleJobPost = ClientPostDataModel(
                    doc["userId"].toString(),
                    doc["title"].toString(),
                    doc["desc"].toString(),
                    doc["category"].toString(),
                    doc["experience"].toString().toInt(),
                    doc["salary"].toString().toInt(),
                    doc["location"].toString(),
                    doc["appliedEmployee"] as Map<String, String>,
                    doc["arrachment"].toString(),
                    doc["timeStamp"].toString().toLong(),
                    doc["companyName"].toString(),
                    doc["genderName"].toString(),
                    doc.id
                )
                if (singleJobPost != null) {
                    myPost.add(singleJobPost)
                }
            }
            mutableLiveData.value=myPost
        }
        return mutableLiveData
    }
}
// suspend fun getAllPost()=Firebase.firestore.collection(auth.toString()).whereEqualTo("userId", auth.uid.toString())
//    suspend fun getJobPost(): ArrayList<ClientPostDataModel> {
//        val arrayList: ArrayList<ClientPostDataModel> = ArrayList()
//
//        val db = Firebase.firestore.collection("JobPost")
//        val query = db.whereEqualTo()
//        query.get().addOnSuccessListener {
//            for (doc in it.documents) {
//                val singleJobPost = ClientPostDataModel(
//                    doc["userId"].toString(),
//                    doc["title"].toString(),
//                    doc["desc"].toString(),
//                    doc["category"].toString(),
//                    doc["experience"].toString().toInt(),
//                    doc["salary"].toString().toInt(),
//                    doc["location"].toString(),
//                    doc["appliedEmployee"] as Map<String, String>,
//                    doc["arrachment"].toString(),
//                    doc["timeStamp"].toString().toLong(),
//                    doc["companyName"].toString(),
//                    doc["genderName"].toString(),
//                    doc.id
//                )
//                if (singleJobPost != null) {
//                    arrayList.add(singleJobPost)
//                }
//            }
//        }
//        return arrayList
//    }
//}