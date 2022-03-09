package com.university_project.jobly.employee.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.datamodel.CallForInterViewDataModel
import com.university_project.jobly.datamodel.PostDataModel

object Repository {
    val dbPost = Firebase.firestore.collection("JobPost")
    private val myJobPost = mutableSetOf<PostDataModel>()
    fun getPostFromDataBase(categoryList: List<String>): MutableLiveData<List<PostDataModel>> {
        Firebase.auth.uid!!
        val query = dbPost.whereArrayContainsAny("category", categoryList)
        val mutableLiveData = MutableLiveData<List<PostDataModel>>()
        query.addSnapshotListener { document, _ ->
            Log.d("TAG", "getResponseUsingLiveData: ${document?.documents}")
            for (dc in document!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> myJobPost.add(
                        addData(
                            "REMOVED",
                            dc
                        )
                    )
                    DocumentChange.Type.REMOVED -> removeFromArray(
                        addData(
                            "REMOVED",
                            dc
                        )
                    )
                    DocumentChange.Type.MODIFIED -> modifiedFromArray(
                        addData(
                            "REMOVED",
                            dc
                        )
                    )
                }
            }
            Log.d("TAGM", "getPostFromDataBase: ${ myJobPost.toTypedArray().asList()}")
            mutableLiveData.value = myJobPost.toTypedArray().asList()
        }
        return mutableLiveData
    }

    private fun modifiedFromArray(addData: PostDataModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myJobPost.removeIf { it.docId == addData.docId }
            myJobPost.add(addData)
        }
    }

    private fun removeFromArray(addData: PostDataModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myJobPost.removeIf { it.docId == addData.docId }
        }
    }

    private fun addData(type: String, m_doc: DocumentChange): PostDataModel {
        val doc = m_doc.document.data
        return PostDataModel(
            doc["userId"].toString(),
            doc["title"].toString(),
            doc["desc"].toString(),
            doc["category"] as ArrayList<String>,
            doc["experience"].toString().toInt(),
            doc["salary"].toString().toInt(),
            doc["location"].toString(),
            doc["appliedEmployee"] as Map<String, String>,
            doc["attachment"].toString(),
            doc["timeStamp"].toString().toLong(),
            doc["companyName"].toString(),
            doc["genderName"].toString(),
            m_doc.document.id,
            doc["call_for_interview"] as ArrayList<CallForInterViewDataModel>
        )
    }
}


