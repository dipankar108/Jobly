package com.university_project.jobly.chatserver

data class ChatDataModel(
    val EmpName: String = "",
    val CltName: String = "",
    val CltId: String = "",
    val EmpId: String = "",
    val postId: String = "",
    val postTitle: String = "",
    val clientSeen:Boolean=false,
    val empSeen:Boolean=false,
    val timeStamp:Long,
    val messages: ArrayList<MessageModel> = ArrayList()
)