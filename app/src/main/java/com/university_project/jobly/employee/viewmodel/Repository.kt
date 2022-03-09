package com.university_project.jobly.employee.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.datamodel.CallForInterViewDataModel
import com.university_project.jobly.datamodel.PostDataModel

object Repository {
    val dbPost = Firebase.firestore.collection("JobPost")
    private val myJobPost = mutableSetOf<PostDataModel>()

    //updating like when user Clicked on the like button
    fun updateLike(docId: String, userId: String, b: Boolean) {
        if (b) {
            dbPost.document(docId).update("like", FieldValue.arrayRemove(userId))
        } else {
            dbPost.document(docId).update("like", FieldValue.arrayUnion(userId))
        }
    }

    fun getPostFromDataBase(categoryList: List<String>): MutableLiveData<List<PostDataModel>> {
        Firebase.auth.uid!!
        val query = dbPost.whereArrayContainsAny("category", categoryList)
        val mutableLiveData = MutableLiveData<List<PostDataModel>>()
        query.addSnapshotListener { document, _ ->
            documentChangesFun(document, "gpfdb")
            mutableLiveData.value = myJobPost.toTypedArray().asList()
        }
        return mutableLiveData
    }

    private fun documentChangesFun(document: QuerySnapshot?, s: String) {
        for (dc in document!!.documentChanges) {
            when (dc.type) {
                DocumentChange.Type.ADDED -> addingPost(dc,s)
                DocumentChange.Type.REMOVED -> removeFromArray(addData(dc))
                DocumentChange.Type.MODIFIED -> modifiedFromArray(addData(dc))
            }
        }
    }

    private fun addingPost(dc: DocumentChange?, s: String) {
        dc?.let {
          if (s=="gpfdb"){
              myJobPost.add(addData(dc))
          }
        }
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

    private fun addData(m_doc: DocumentChange): PostDataModel {
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
            doc["call_for_interview"] as ArrayList<CallForInterViewDataModel>,
            isValueNull(doc["like"])
        )
    }

    private fun isValueNull(any: Any?): ArrayList<String> {
        Log.d("TAG", "isValueNull: $any")
        if (any == null) {
            return arrayListOf("")
        }
        return any as ArrayList<String>
    }
}


