package com.university_project.jobly.chatserver

data class ChatListViewDataModel(
    val empName: String = "",
    val cltName: String = "",
    val jobtitle: String = "",
    val clientSeen: Boolean = false,
    val empSeen: Boolean = false,
    val timeStamp: Long = 0,
    var docId: String = "",
    val clientProfileImg: String = "",
    val empProfileImg: String = "",
    val lastClientMessage: String = "",
    val lastEmpMessage: String = "",
    val lastClientMessageTime: Long = 0,
    val lastEmpMessageTime: Long = 0
)