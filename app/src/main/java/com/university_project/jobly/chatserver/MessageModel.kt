package com.university_project.jobly.chatserver

data class MessageModel(
    val link: String = "No Image",
    val timeStamp: Long = 0,
    val userId: String = "",
    val message: String = "",
    val userType: String = ""
)
