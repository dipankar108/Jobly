package com.university_project.jobly.chatserver

data class ChatListViewDataModel(
    val empName: String = "",
    val cltName: String = "",
    val postTitle: String = "",
    val clientSeen: Boolean = false,
    val empSeen: Boolean = false,
    val timeStamp: Long = 0,
    val docId: String = "",
)