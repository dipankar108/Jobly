package com.university_project.jobly.chatserver

data class ChatDataModel(
    val empName: String = "",
    val cltName: String = "",
    val cltId: String = "",
    val empId: String = "",
    val postId: String = "",
    val postTitle: String = "",
    val clientSeen:Boolean=false,
    val empSeen:Boolean=false,
    val timeStamp:Long=0,
    val docId:String="",
    val clientProfileImg: String = "",
    val empProfileImg:String ="",
    val messages: ArrayList<MessageModel> = ArrayList()
)