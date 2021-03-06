package com.university_project.jobly.baseviewmodel

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.university_project.jobly.R
import com.university_project.jobly.chatserver.ChatDataModel
import com.university_project.jobly.chatserver.ChatListViewDataModel
import com.university_project.jobly.chatserver.MessageModel
import com.university_project.jobly.datamodel.*

object Repository {
    private val auth = Firebase.auth
    private val dbPost = Firebase.firestore.collection("JobPost")
    private val chatServer = Firebase.firestore.collection("ChatServer")
    private val dbProfile = Firebase.firestore.collection("User")
    private val verificationCollection = Firebase.firestore.collection("Verification")
    private val myJobPost = mutableSetOf<PostDataModel>()
    private val fabEmpPost = mutableSetOf<PostDataModel>()
    private val empProfilePost = mutableSetOf<EmployeeProfileModel>()
    private val myApplication = mutableSetOf<PostDataModel>()
    private val chatList = mutableSetOf<ChatListViewDataModel>()
    private var storageRef = Firebase.storage
    var liveSkill = MutableLiveData<List<String>>()

    /**SIGNING OUT FROM THE DEVICE**/

    /**GETTING EMPLOYEE SKILL LIST**/
    fun getMySkill(): LiveData<List<String>> {
        dbProfile.document(auth.uid.toString()).addSnapshotListener { value, _ ->
            value?.data?.get("skill")?.let { list ->
                liveSkill.value = list as List<String>
            }
        }
        return liveSkill
    }

    /**VERIFIED STATUS**/
    fun isVerified(userType: String): LiveData<Boolean> {
        val status = MutableLiveData(true)
        if (userType == "Client") {
            dbProfile.document(auth.uid.toString()).addSnapshotListener { value, _ ->
                val dbProfile = value?.toObject(ClientProfileModel::class.java)
                status.value = dbProfile?.verify
            }
        } else {
            dbProfile.document(auth.uid.toString()).addSnapshotListener { value, _ ->
                val dbProfile = value?.toObject(EmployeeProfileModel::class.java)
                status.value = dbProfile?.verify
            }
        }
        return status
    }

    /** Updating active or not  **/
    fun updateActiveStatus(onStartApp: Boolean) {
        if (onStartApp) {
            dbProfile.document(auth.uid.toString()).update("active", true)
        } else {
            dbProfile.document(auth.uid.toString()).update("active", false).addOnSuccessListener {
                Log.d("TAGService", "updateActiveStatus: called")
            }.addOnFailureListener {
                Log.d("TAGService", "updateActiveStatus: failed")
            }
        }
    }

    /**  GETTING MESSAGE LIST **/
    fun getMessage(docId: String): LiveData<ChatDataModel> {
        val liveChat = MutableLiveData<ChatDataModel>()
        chatServer.document(docId).addSnapshotListener { chatValue, _ ->
            chatValue?.let {
                val chatDataModel = it.toObject(ChatDataModel::class.java)
                liveChat.value = chatDataModel!!
            }
        }
        return liveChat
    }

    /** Getting chat List For user **/
    fun getChatList(userType: String, userId: String): LiveData<List<ChatListViewDataModel>> {
        val liveChatList = MutableLiveData<List<ChatListViewDataModel>>()
        chatServer.whereEqualTo(userType, userId)
            .addSnapshotListener { value, _ ->
                value?.let { local ->
                    for (doc in local.documentChanges) {
                        val chatListViewDataModel =
                            doc.document.toObject(ChatListViewDataModel::class.java)
                        chatListViewDataModel.docId = doc.document.id
                        if (DocumentChange.Type.MODIFIED == doc.type) {
                            chatList.removeIf { it.docId == chatListViewDataModel.docId }
                            chatList.add(chatListViewDataModel)
                        }
                        if (DocumentChange.Type.ADDED == doc.type) {
                            chatList.add(chatListViewDataModel)
                        }
                    }
                    /**
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
                     **/
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
    fun createJobPost(
        postTitle: String,
        postDesc: String,
        postExperience: Int,
        postSkills: MutableList<String>,
        postSalary: Int,
        postGender: String,
        isLike: ArrayList<String>,
        callforinterview: ArrayList<CallForInterViewDataModel>,
        timeStamp: Long,
        appliedEmployee: ArrayList<AppliedDataModel>,
        attachmentLink: Uri
    ): LiveData<Boolean> {
        val isDone = MutableLiveData(false)
        val mstorageRef =
            storageRef.reference.child("attachPDF/${System.currentTimeMillis()}${Firebase.auth.uid}")
        dbProfile.document(auth.uid.toString()).get().addOnSuccessListener { value ->
            value?.let { doc ->
                val clientProfileModel = doc.toObject(ClientProfileModel::class.java)
                clientProfileModel?.let {
                    if (attachmentLink != Uri.EMPTY) {
                        mstorageRef.putFile(attachmentLink).addOnSuccessListener {
                            mstorageRef.downloadUrl.addOnSuccessListener { attachUrl ->
                                if (attachUrl != null) {
                                    val createPostModel = CreatePostModel(
                                        auth.uid.toString(),
                                        postTitle,
                                        postDesc,
                                        postSkills,
                                        postExperience,
                                        postSalary,
                                        clientProfileModel.companyLocation,
                                        arrayListOf(),
                                        arrayListOf(),
                                        attachUrl.toString(),
                                        timeStamp,
                                        clientProfileModel.companyName,
                                        postGender,
                                        arrayListOf(),
                                        arrayListOf()
                                    )
                                    dbPost.document().set(createPostModel).addOnSuccessListener {
                                        isDone.value = true
                                    }.addOnFailureListener { e ->
                                        Log.d("TAG", "createJobPost: ${e.message}")
                                    }
                                }
                            }
                        }.addOnFailureListener { e ->
                            Log.d("TAG", "createJobPostPDF: ${e.message}")
                        }
                    } else {
                        val createPostModel = CreatePostModel(
                            auth.uid.toString(),
                            postTitle,
                            postDesc,
                            postSkills,
                            postExperience,
                            postSalary,
                            clientProfileModel.companyLocation,
                            arrayListOf(),
                            arrayListOf(),
                            "No Attachment",
                            timeStamp,
                            clientProfileModel.companyName,
                            postGender,
                            arrayListOf(),
                            arrayListOf()
                        )
                        dbPost.document().set(createPostModel).addOnSuccessListener {
                            isDone.value = true
                        }.addOnFailureListener { e ->
                            Log.d("TAG", "createJobPost: ${e.message}")
                        }
                    }

                }
            }
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

    /**GETTING CLIENT PROFILE**/
    fun getClientProfile(): LiveData<ClientProfileModel> {
        val liveData = MutableLiveData<ClientProfileModel>()
        dbProfile.document(Firebase.auth.uid.toString()).addSnapshotListener { data, error ->
            data?.let {
                liveData.value = it.toObject(ClientProfileModel::class.java)
            }
        }
        return liveData
    }

    /**GETTING EMP PROFILE**/
    fun getEmpProfile(): LiveData<EmployeeProfileModel> {
        val liveData = MutableLiveData<EmployeeProfileModel>()
        dbProfile.document(Firebase.auth.uid.toString()).addSnapshotListener { data, _ ->
            data?.let {
                liveData.value = it.toObject(EmployeeProfileModel::class.java)
            }
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
            document?.let {
                documentChangesFun(document, "getAllPost")
            }
            mutableLiveData.value = myJobPost.toTypedArray().asList()
        }
        return mutableLiveData
    }

    //getting post for employee
    fun getEmpPost(categoryList: List<String>): MutableLiveData<List<PostDataModel>> {
        val query = dbPost.whereArrayContainsAny("skill", categoryList)
        val mutableLiveData = MutableLiveData<List<PostDataModel>>()
        var listCom = listOf<String>()
        var filterData = mutableSetOf<PostDataModel>()
        dbProfile.document(auth.uid.toString()).addSnapshotListener { value, _ ->
            value?.data?.get("company")?.let { list ->
                listCom = list as List<String>
                query.addSnapshotListener { document, _ ->
                    document?.let {
                        documentChangesFun(document, "getAllPost")
                    }
                    if (listCom.isNotEmpty()) {
                        myJobPost.forEach {
                            if (listCom.contains(it.companyName)) {
                                filterData.add(it)
                            }
                        }
                        mutableLiveData.value = filterData.toTypedArray().asList()
                    } else {
                        mutableLiveData.value = myJobPost.toTypedArray().asList()
                    }

                }
            }
        }
        return mutableLiveData
    }

    /** Getting single post **/
    fun singlePost(docId: String): LiveData<PostDataModel> {
        var post = MutableLiveData<PostDataModel?>()
        dbPost.document(docId).addSnapshotListener { document, _ ->
            document?.let {
                val singlePost = it?.toObject(PostDataModel::class.java)
                post.value = singlePost
                // post.value = document.data?.let { it1 -> addData(it1, docId) }
            }
        }
        return post as LiveData<PostDataModel>
    }

    /** Getting Category **/
    fun getSkill(): LiveData<List<String>> {
        val skills = MutableLiveData<List<String>>()
        Firebase.firestore.collection("Category").document("Skill").get().addOnSuccessListener {
            skills.value = it.data?.get("skill") as List<String>
        }.addOnFailureListener {
            Log.d("TAG", "getSkill: ${it.message}")
        }
        return skills
    }

    /**UPDATING SKILL **/
    fun updateSkill(updateType: String, skill: String) {
        if (updateType == "union") {
            dbProfile.document(auth.uid.toString())
                .update("skill", FieldValue.arrayUnion(skill))
        } else {
            dbProfile.document(auth.uid.toString())
                .update("skill", FieldValue.arrayRemove(skill))
        }
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
        dbProfile.document(auth.uid.toString()).get().addOnSuccessListener { value ->
            val userData = value?.toObject(EmployeeProfileModel::class.java)
            Log.d("TAG", "applyForPost: ${userData?.cvEmp}")
            userData?.let {
                if (userData.cvEmp != "") {
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

    fun createChatDoc(appliedDataModel: AppliedDataModel) {
        dbProfile.document(Firebase.auth.uid.toString()).get().addOnSuccessListener { it ->
            val clientName = "${it["fname"]} ${it["lname"]}"
            val empName = appliedDataModel.fullName
            val CltId = auth.uid.toString()
            val EmpId = appliedDataModel.employeeId
            val postId = appliedDataModel.docId
            val postTitle = "This is post"
            val messages = arrayListOf(
                MessageModel(
                    "No Image",
                    System.currentTimeMillis(),
                    auth.uid.toString(),
                    "Hi",
                    "Client"
                )
            )
            var myChatDataModel = ChatDataModel()
            dbProfile.document(CltId).get().addOnSuccessListener { clientVal ->
                val clientProfileImg = clientVal.data!!["profileImg"].toString()
                dbProfile.document(EmpId).get().addOnSuccessListener { empVal ->
                    val empProfileImg = empVal.data!!["profileImg"].toString()
                    dbPost.document(appliedDataModel.docId).get().addOnSuccessListener {
                        val postData = it.toObject(PostDataModel::class.java)
                        Log.d("TAGA", "createChatDoc: ${postData?.title}")
                        if (postData != null) {
                            myChatDataModel = ChatDataModel(
                                postData.title,
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
                                messages,
                                "No Message",
                                "No Message",
                                0,
                                0
                            )
                            chatServer.add(myChatDataModel)
                        }
                    }

                }
            }
        }

    }

    fun sendMessage(docId: String, messageModel: MessageModel) {
        val sendMessageId = chatServer.document(docId)
        if (messageModel.userType == "Client") {
            sendMessageId.update("lastClientMessage", messageModel.message)
            sendMessageId.update("lastClientMessageTime", System.currentTimeMillis())

        } else {
            sendMessageId.update("lastEmpMessage", messageModel.message)
            sendMessageId.update("lastEmpMessageTime", System.currentTimeMillis())
        }
        sendMessageId.update("messages", FieldValue.arrayUnion(messageModel))
            .addOnSuccessListener {
                sendMessageId.update("timeStamp", System.currentTimeMillis())
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
        dbPost.document(appliedDataModel.docId)
            .update("appliedEmployee", FieldValue.arrayRemove(appliedDataModel))
            .addOnSuccessListener {
                val appliedEmp = AppliedDataModel(
                    appliedDataModel.docId,
                    appliedDataModel.cvAttachment,
                    appliedDataModel.employeeId,
                    appliedDataModel.profileImage,
                    appliedDataModel.fullName,
                    true
                )
                dbPost.document(appliedDataModel.docId)
                    .update("appliedEmployee", FieldValue.arrayUnion(appliedEmp))
            }

        // dbPost.document(appliedDataModel.docId).set(false, SetOptions.mergeFieldPaths("appliedEmployee" ))
    }

    fun isUserActive(userId: String): LiveData<Boolean> {
        var userActive = MutableLiveData<Boolean>()
        dbProfile.document(userId).addSnapshotListener { value, _ ->
            val userData = value?.toObject(EmployeeProfileModel::class.java)
            userActive.value = userData?.active
        }
        return userActive
    }

    fun getCompany(): LiveData<List<String>> {
        val comps = MutableLiveData<List<String>>()
        Firebase.firestore.collection("CompanyList").document("companylist").get()
            .addOnSuccessListener {
                comps.value = it.data?.get("companynamelist") as List<String>
            }.addOnFailureListener {
                Log.d("TAG", "getCompany: ${it.message}")
            }
        return comps
    }

    fun setVerfication(pdfUri: Uri, userType: String, context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.progressbarlayout)
        dialog.setCancelable(false)
        dialog.show()

        val mstorageRef =
            storageRef.reference.child("attachverPDF/${System.currentTimeMillis()}${Firebase.auth.uid}")
        dbProfile.document(auth.uid.toString()).addSnapshotListener { value, error ->
            value?.let { doc ->
                mstorageRef.putFile(pdfUri).addOnSuccessListener {
                    mstorageRef.downloadUrl.addOnSuccessListener { attachUrl ->
                        if (attachUrl != null) {
                            val verificationModel = VerificationModel(
                                attachUrl.toString(),
                                auth.uid.toString(),
                                System.currentTimeMillis(),
                                userType
                            )
                            verificationCollection.document(auth.uid.toString())
                                .set(verificationModel)
                            dialog.dismiss()
                            Toast.makeText(
                                context,
                                "Verification file send successfully",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }.addOnFailureListener { e ->
                        Log.d("TAG", "createJobPost: ${e.message}")
                        Toast.makeText(context, "Error occured : ${e.message}", Toast.LENGTH_LONG)
                            .show()
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    fun updateCompany(updateType: String, company: String) {
        if (updateType == "union") {
            dbProfile.document(auth.uid.toString())
                .update("company", FieldValue.arrayUnion(company))
        } else {
            dbProfile.document(auth.uid.toString())
                .update("company", FieldValue.arrayRemove(company))
        }
    }


    fun updatePost(type: String, text: String, docID: String) {
        if (type == "experience") {
            dbPost.document(docID).update(type, text.toInt())
        } else if (type == "Salary") {
            dbPost.document(docID).update(type, text)
        } else {
            dbPost.document(docID).update(type, text)
        }
    }
}

