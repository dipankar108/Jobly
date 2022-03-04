package com.university_project.jobly.client.clientviewmodel

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.client.datamodel.ClientPostDataModel
import java.util.*

object PostRepo {
    private val auth = Firebase.auth
    private val myPost = mutableSetOf<ClientPostDataModel>()
    fun getResponseUsingLiveData(): MutableLiveData<List<ClientPostDataModel>> {
        val mutableLiveData = MutableLiveData<List<ClientPostDataModel>>()
        val db = Firebase.firestore.collection("JobPost")
        val query = db.whereEqualTo("userId", auth.uid.toString())
        query.addSnapshotListener { document, _ ->
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

    private fun modifiedFromArray(addData: ClientPostDataModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myPost.removeIf { it.docId == addData.docId }
            myPost.add(addData)
        }
        /**
        myPost.forEachIndexed { index, dt ->
        dt.takeIf { it.docId == addData.docId }?.let {

        myPost[index]=addData
        }
        }
         **/
    }

    private fun removeFromArray(addData: ClientPostDataModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myPost.removeIf { it.docId == addData.docId }
        }
    }

    private fun addData(type: String, m_doc: DocumentChange): ClientPostDataModel {
        val doc = m_doc.document.data
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
            m_doc.document.id,
            type
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