package com.university_project.jobly.baseviewmodel

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.university_project.jobly.chatserver.ChatDataModel
import com.university_project.jobly.chatserver.ChatListViewDataModel
import com.university_project.jobly.chatserver.MessageModel
import com.university_project.jobly.datamodel.AppliedDataModel
import com.university_project.jobly.datamodel.CreatePostModel
import com.university_project.jobly.datamodel.EmployeeProfileModel
import com.university_project.jobly.datamodel.PostDataModel

object Repository {
    private val auth = Firebase.auth
    private val dbPost = Firebase.firestore.collection("JobPost")
    private val chatServer = Firebase.firestore.collection("ChatServer")
    private val dbProfile = Firebase.firestore.collection("User")
    private val myJobPost = mutableSetOf<PostDataModel>()
    private val fabEmpPost = mutableSetOf<PostDataModel>()
    private val empProfilePost = mutableSetOf<EmployeeProfileModel>()
    private val myApplication = mutableSetOf<PostDataModel>()
    private val chatList = mutableSetOf<ChatListViewDataModel>()
    private var storageRef = Firebase.storage

    /**GETTING EMPLOYEE SKILL LIST**/
    fun getMySkill(): LiveData<List<String>> {
        var liveSkill = MutableLiveData<List<String>>()
        dbProfile.document(auth.uid.toString()).addSnapshotListener { value, error ->
            value?.data?.get("skill")?.let {
                liveSkill.value=it as List<String>
            }
        }
        return liveSkill
    }

    /**  GETTING MESSAGE LIST **/
    fun getMessage(docId: String): LiveData<ChatDataModel> {
        val liveChat = MutableLiveData<ChatDataModel>()
        chatServer.document(docId).addSnapshotListener { chatValue, _ ->
            val chatDataModel = chatValue?.toObject(ChatDataModel::class.java)
            chatDataModel?.let {
                liveChat.value = it
            }
        }
        return liveChat
    }

    /** Getting chat List For user **/
    fun getChatList(userType: String, userId: String): LiveData<List<ChatListViewDataModel>> {
        val liveChatList = MutableLiveData<List<ChatListViewDataModel>>()
        chatServer.whereEqualTo(userType, userId).addSnapshotListener { value, _ ->
    value?.let {
        for (doc in value!!.documentChanges) {
            val res = doc.document.data
            val empName = res["empName"].toString()
            val cltName = res["cltName"].toString()
            val cltId = res["cltId"].toString()
            val empId = res["empId"].toString()
            val postId = res["postId"].toString()
            val postTitle = res["postTitle"].toString()
            val clientSeen = res["clientSeen"] as Boolean
            val empSeen = res["empSeen"] as Boolean
            val timeStamp = res["timeStamp"] as Long
            val docId = doc.document.id
            val clientProfileImg = res["cilentProfileImg"].toString()
            val empProfileImg = res["empProfileImg"].toString()
            chatList.add(
                ChatListViewDataModel(
                    empName,
                    cltName,
                    postTitle,
                    clientSeen,
                    empSeen,
                    timeStamp,
                    docId,
                    clientProfileImg,
                    empProfileImg
                )
            )
        }
    }
            liveChatList.value = chatList.toTypedArray().toList()
        }
        return liveChatList
    }

    /** Getting applied post for the Employee**/
    fun getEmpAppliedPost(): LiveData<List<PostDataModel>> {
        val getMYApplication = MutableLiveData<List<PostDataModel>>()
        dbPost.whereArrayContains("employeeId", auth.uid.toString())
            .addSnapshotListener { value, _ ->
                value?.let {
                    documentChangesFun(value, "myapplication")
                }
                getMYApplication.value = myApplication.toList()
            }
        return getMYApplication
    }

    /** Creating Post **/
    fun createJobPost(createPostModel: CreatePostModel): LiveData<Boolean> {
        val isDone = MutableLiveData(false)
        dbPost.document().set(createPostModel).addOnSuccessListener {
            isDone.value = true
        }
        return isDone
    }

    /** getting fab post **/

    fun getFabPost(): MutableLiveData<List<PostDataModel>> {
        val query = dbPost.whereArrayContains("like", Firebase.auth.uid.toString())
        val mutableLiveData = MutableLiveData<List<PostDataModel>>()
        query.addSnapshotListener { document, _ ->
            document?.let {
                documentChangesFun(document, "getEmpFabPost")
            }
            mutableLiveData.value = fabEmpPost.toTypedArray().asList()
        }
        return mutableLiveData
    }

    fun getEmpProfile(): LiveData<EmployeeProfileModel> {
        val liveData = MutableLiveData<EmployeeProfileModel>()
        dbProfile.document(Firebase.auth.uid.toString()).get().addOnSuccessListener {
            liveData.value = it.toObject(EmployeeProfileModel::class.java)
        }
        return liveData
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
    fun getAppliedEmployeeList(docId: String): MutableLiveData<List<AppliedDataModel>> {
        val appliedEmployeeList = MutableLiveData<List<AppliedDataModel>>()
        var appliedDataModel = mutableListOf<AppliedDataModel>()
        dbPost.document(docId).addSnapshotListener { doc, _ ->
            val obj = doc?.toObject(PostDataModel::class.java)
            if (obj != null) {
                appliedDataModel = obj.appliedEmployee
            }
            appliedEmployeeList.value = appliedDataModel
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
        val query = dbPost.whereArrayContainsAny("skill", categoryList)
        val mutableLiveData = MutableLiveData<List<PostDataModel>>()
        query.addSnapshotListener { document, _ ->
            document?.let {
                documentChangesFun(document, "getAllPost")
            }
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
                "myapplication" -> myApplication.add(addData(dc.document.data, dc.document.id))
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
            doc["skill"] as ArrayList<String>,
            doc["experience"].toString().toInt(),
            doc["salary"].toString().toInt(),
            doc["location"].toString(),
            doc["appliedEmployee"] as ArrayList<AppliedDataModel>,
            isValueNull(doc["employeeId"]) as ArrayList<String>,
            doc["attachment"].toString(),
            doc["timeStamp"].toString().toLong(),
            doc["companyName"].toString(),
            doc["genderName"].toString(),
            docId,
            doc["call_for_interview"] as ArrayList<String>,
            isValueNull(doc["like"]) as ArrayList<String>
        )
    }

    private fun isValueNull(any: Any?): Any? {
        if (any == null) {
            return arrayListOf(any)
        }
        return any
    }

    fun updateEmployeeProfile(employeeProfileModel: EmployeeProfileModel) {
        dbProfile.document(auth.uid.toString()).set(employeeProfileModel).addOnSuccessListener {
            Log.d("TAG", "updateEmployeeProfile: Updated")
        }
    }

    fun applyForPost(docId: String): LiveData<String> {
        val mutableString = MutableLiveData("start")
        dbProfile.document(auth.uid.toString()).addSnapshotListener { value, _ ->
            val userData = value?.toObject(EmployeeProfileModel::class.java)
            Log.d("TAG", "applyForPost: ${userData?.cvEmp}")
            userData?.let {
                if (userData.cvEmp.isNotEmpty()) {
                    val appliedDataModel = userData?.let {
                        AppliedDataModel(
                            docId,
                            it.cvEmp,
                            auth.uid.toString(),
                            userData.profileImg,
                            "${userData.fname} ${userData.lname}"
                        )
                    }
                    dbPost.document(docId)
                        .update("appliedEmployee", FieldValue.arrayUnion(appliedDataModel))
                    dbPost.document(docId)
                        .update("employeeId", FieldValue.arrayUnion(auth.uid.toString()))
                        .addOnSuccessListener {
                            mutableString.value = "uploaded"
                        }.addOnFailureListener {
                            mutableString.value = "failed"
                        }
                } else {
                    mutableString.value = "nocv"
                }
            }
        }
        return mutableString

    }

    fun createChatDoc(chatDataModel: AppliedDataModel) {
        dbProfile.document(Firebase.auth.uid.toString()).get().addOnSuccessListener { it ->
            val clientName = "${it["fname"]} ${it["lname"]}"
            val empName = chatDataModel.fullName
            val CltId = auth.uid.toString()
            val EmpId = chatDataModel.employeeId
            val postId = chatDataModel.docId
            val postTitle = "This is post"
            val messages = ArrayList<MessageModel>()
            var myChatDataModel = ChatDataModel()
            dbProfile.document(CltId).get().addOnSuccessListener { clientVal ->
                val clientProfileImg = clientVal.data!!["profileImg"].toString()
                dbProfile.document(EmpId).get().addOnSuccessListener { empVal ->
                    val empProfileImg = empVal.data!!["profileImg"].toString()
                    myChatDataModel = ChatDataModel(
                        empName,
                        clientName,
                        CltId,
                        EmpId,
                        postId,
                        postTitle,
                        false,
                        false,
                        System.currentTimeMillis(),
                        "",
                        clientProfileImg,
                        empProfileImg,
                        messages
                    )
                    Log.d("TAG", "createChatDoc: $chatDataModel")
                    chatServer.add(myChatDataModel)
                }
            }
        }

    }

    fun sendMessage(docId: String, messageModel: MessageModel) {
        chatServer.document(docId).update("messages", FieldValue.arrayUnion(messageModel))
            .addOnSuccessListener {
                Log.d("TAG", "sendMessage: Message Send")
            }
    }

    fun uploadCV(uri: Uri): LiveData<List<String>> {
        var liveData = MutableLiveData<List<String>>()
        var liveString = listOf<String>()
        val mstorageRef =
            storageRef.reference.child("cvPDF/${System.currentTimeMillis()}${auth.uid}")
        mstorageRef.putFile(uri)
            .addOnSuccessListener {
                mstorageRef.downloadUrl.addOnSuccessListener {
                    liveString = listOf(it.toString())
                }
            }
        liveData.value = liveString
        Log.d("TAGA", "uploadCV: ${liveData.value}")
        return liveData
    }

    fun updateCallforInterView(docId: String, employeeId: String) {
        dbPost.document(docId).update("call_for_interview", FieldValue.arrayUnion(employeeId))
    }

    fun updateAppliedEmployye(appliedDataModel: AppliedDataModel) {
        val appliedEmp = AppliedDataModel(
            appliedDataModel.docId,
            appliedDataModel.cvAttachment,
            appliedDataModel.employeeId,
            appliedDataModel.profileImage,
            appliedDataModel.fullName,
            true
        )
        // dbPost.document(appliedDataModel.docId).set(false, SetOptions.mergeFieldPaths("appliedEmployee" ))
    }
}


