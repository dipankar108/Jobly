package com.university_project.jobly.client.clientviewmodel

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object PostRepo {

    suspend fun getJobPost(): ArrayList<ClientPostDataModel> {
        val arrayList: ArrayList<ClientPostDataModel> = ArrayList()
        val auth = Firebase.auth
        val db = Firebase.firestore.collection("JobPost")
        val query = db.whereEqualTo("userId", auth.uid.toString())
        query.get().addOnSuccessListener {
            for (doc in it.documents) {
                val clientjobpostdatamodel = doc.toObject(ClientPostDataModel::class.java)
                if (clientjobpostdatamodel != null) {
                    arrayList.add(clientjobpostdatamodel)
                }
            }
        }
        return arrayList
    }
}