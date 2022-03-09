package com.university_project.jobly.client.clientviewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.client.datamodel.AppliedEmployeeDataModel
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.datamodel.CallForInterViewDataModel

object PostRepo {
    private val auth = Firebase.auth
    val dbPost = Firebase.firestore.collection("JobPost")
    private val myPost = mutableSetOf<PostDataModel>()
    fun getResponseUsingLiveData(): MutableLiveData<List<PostDataModel>> {
        val mutableLiveData = MutableLiveData<List<PostDataModel>>()
        val query = dbPost.whereEqualTo("userId", auth.uid.toString())
        query.addSnapshotListener { document, _ ->
            Log.d("TAG", "getResponseUsingLiveData: ${document?.documents}")
            for (dc in document!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> myPost.add(addData("REMOVED", dc))
                    DocumentChange.Type.REMOVED -> removeFromArray(addData("REMOVED", dc))
                    DocumentChange.Type.MODIFIED -> modifiedFromArray(addData("REMOVED", dc))
                }
            }
            mutableLiveData.value = myPost.toTypedArray().asList()
        }
        return mutableLiveData
    }

    fun getAppliedEmployeeList(docId: String): MutableLiveData<List<AppliedEmployeeDataModel>> {
        val appliedEmployeeList = MutableLiveData<List<AppliedEmployeeDataModel>>()
        dbPost.document(docId).addSnapshotListener { mdoc, _ ->
            if (mdoc != null) {
                mdoc.data?.let { doc ->
                    val myMap = doc["appliedEmployee"] as Map<String, String>
                    if (myMap.isNotEmpty()) {
                        appliedEmployeeList.value = listOf(
                            AppliedEmployeeDataModel(
                                doc["userId"].toString(),
                                myMap,
                                docId,
                                doc["call_for_interview"] as ArrayList<CallForInterViewDataModel>
                            )
                        )
                    }
                }

            }

        }
        return appliedEmployeeList
    }

    private fun modifiedFromArray(addData: PostDataModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myPost.removeIf { it.docId == addData.docId }
            myPost.add(addData)
        }
    }

    private fun removeFromArray(addData: PostDataModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myPost.removeIf { it.docId == addData.docId }
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
    /**
    private fun convertToObject(doc: DocumentSnapshot, type: String): ClientPostDataModel {
    return ClientPostDataModel(
    doc["userId"].toString(),
    doc["title"].toString(),
    doc["desc"].toString(),
    doc["category"].toString(),
    doc["experience"].toString().toInt(),
    doc["salary"].toString().toInt(),
    doc["location"].toString(),
    doc["appliedEmployee"] as Map<String, String>,
    doc["attachment"].toString(),
    doc["timeStamp"].toString().toLong(),
    doc["companyName"].toString(),
    doc["genderName"].toString(),
    doc.id,
    type
    )
    }
     **/
}
/**
suspend fun getAllPost()=Firebase.firestore.collection(auth.toString()).whereEqualTo("userId", auth.uid.toString())
suspend fun getJobPost(): ArrayList<ClientPostDataModel> {
val arrayList: ArrayList<ClientPostDataModel> = ArrayList()

val db = Firebase.firestore.collection("JobPost")
val query = db.whereEqualTo()
query.get().addOnSuccessListener {
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
arrayList.add(singleJobPost)
}
}
}
return arrayList
}
 **/