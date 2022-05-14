package com.university_project.jobly.chatserver

data class ChatDataModel(
    val jobtitle: String = "",
    val empName: String = "",
    val cltName: String = "",
    val cltId: String = "",
    val empId: String = "",
    val postId: String = "",
    val postTitle: String = "",
    val clientSeen: Boolean = false,
    val empSeen: Boolean = false,
    val timeStamp: Long = 0,
    var docId: String = "",
    val clientProfileImg: String = "",
    val empProfileImg: String = "",
    val messages: ArrayList<MessageModel> = ArrayList(),
    val lastClientMessage: String = "",
    val lastEmpMessage: String = "",
    val lastClientMessageTime: Long = 0,
    val lastEmpMessageTime: Long = 0
)