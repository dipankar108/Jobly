package com.university_project.jobly.baseviewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.client.datamodel.AppliedEmployeeDataModel
import com.university_project.jobly.datamodel.CallForInterViewDataModel
import com.university_project.jobly.datamodel.PostDataModel

object Repository {
    private val auth = Firebase.auth
    private val dbPost = Firebase.firestore.collection("JobPost")
    private val myJobPost = mutableSetOf<PostDataModel>()
    private val fabEmpPost = mutableSetOf<PostDataModel>()

    /** getting fab post **/

    fun getFabPost(): MutableLiveData<List<PostDataModel>> {
        val query = dbPost.whereArrayContains("like", Firebase.auth.uid.toString())
        val mutableLiveData = MutableLiveData<List<PostDataModel>>()

        query.addSnapshotListener { document, _ ->
            documentChangesFun(document, "getEmpFabPost")
            mutableLiveData.value = fabEmpPost.toTypedArray().asList()
        }
        return mutableLiveData
    }

    /** updating like when user Clicked on the like button **/

    fun updateLike(docId: String, userId: String, b: Boolean) {
        if (b) {
            dbPost.document(docId).update("like", FieldValue.arrayRemove(userId))
        } else {
            dbPost.document(docId).update("like", FieldValue.arrayUnion(userId))
        }
    }

    /** deleting client post **/

    fun deletePost(docId: String) {
        dbPost.document(docId).delete()
    }

    /**  Getting Applied employee **/
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

    /** getting all post for client **/
    fun getCltPost(): MutableLiveData<List<PostDataModel>> {
        val mutableLiveData = MutableLiveData<List<PostDataModel>>()
        val query = dbPost.whereEqualTo("userId", auth.uid.toString())
        query.addSnapshotListener { document, _ ->
            documentChangesFun(document, "getAllPost")
            mutableLiveData.value = myJobPost.toTypedArray().asList()
        }
        return mutableLiveData
    }

    //getting post for employee
    fun getEmpPost(categoryList: List<String>): MutableLiveData<List<PostDataModel>> {
        Firebase.auth.uid!!
        val query = dbPost.whereArrayContainsAny("category", categoryList)
        val mutableLiveData = MutableLiveData<List<PostDataModel>>()
        query.addSnapshotListener { document, _ ->
            documentChangesFun(document, "getAllPost")
            mutableLiveData.value = myJobPost.toTypedArray().asList()
        }
        return mutableLiveData
    }

    /** Getting single post **/
    fun singlePost(docId: String): LiveData<PostDataModel> {
        var post = MutableLiveData<PostDataModel>()
        dbPost.document(docId).addSnapshotListener { document, _ ->
            post.value = addData(document?.data!!, docId)
        }
        return post
    }

    /** Getting Category **/
    fun getSkill(): LiveData<List<String>> {
        val skills = MutableLiveData<List<String>>()
        Firebase.firestore.collection("Category").document("Skill").get().addOnSuccessListener {
            skills.value = it.data?.get("skill") as List<String>
        }
        return skills
    }

    private fun documentChangesFun(document: QuerySnapshot?, s: String) {
        for (dc in document!!.documentChanges) {
            when (dc.type) {
                DocumentChange.Type.ADDED -> addingPostToArray(dc, s)
                DocumentChange.Type.REMOVED -> removeFromArray(
                    addData(
                        dc.document.data,
                        dc.document.id
                    ), s
                )
                DocumentChange.Type.MODIFIED -> modifiedFromArray(
                    addData(
                        dc.document.data,
                        dc.document.id
                    ), s
                )
            }
        }
    }

    private fun addingPostToArray(dc: DocumentChange?, s: String) {
        dc?.let {
            when (s) {
                "getAllPost" -> myJobPost.add(addData(dc.document.data, dc.document.id))
                "getEmpFabPost" -> fabEmpPost.add(addData(dc.document.data, dc.document.id))
                else -> {
                }
            }
        }
    }

    private fun modifiedFromArray(singlePost: PostDataModel, s: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when (s) {
                "getAllPost" -> {
                    myJobPost.removeIf { it.docId == singlePost.docId }
                    myJobPost.add(singlePost)
                }
                "getEmpFabPost" -> {
                    fabEmpPost.removeIf { it.docId == singlePost.docId }
                    fabEmpPost.add(singlePost)
                }
            }
        }
    }

    private fun removeFromArray(singlePost: PostDataModel, s: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when (s) {
                "getAllPost" -> {
                    myJobPost.removeIf { it.docId == singlePost.docId }
                }
                "getEmpFabPost" -> {
                    fabEmpPost.removeIf { it.docId == singlePost.docId }
                }
            }

        }
    }

    private fun addData(doc: MutableMap<String, Any>, docId: String): PostDataModel {
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
            docId,
            doc["call_for_interview"] as ArrayList<CallForInterViewDataModel>,
            isValueNull(doc["like"]) as ArrayList<String>
        )
    }

    private fun isValueNull(any: Any?): Any? {
        if (any == null) {
            return arrayListOf(any)
        }
        return any
    }
}


