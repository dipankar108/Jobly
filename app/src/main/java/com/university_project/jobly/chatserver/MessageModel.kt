package com.university_project.jobly.chatserver

data class MessageModel(
    val link: String = "No Image",
    val timeStamp: Long = 0,
    val userId: String = "none",
    val message: String = "none",
    val userType: String = "none"
)
