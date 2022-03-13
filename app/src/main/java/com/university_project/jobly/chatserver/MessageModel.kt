package com.university_project.jobly.chatserver

data class MessageModel(
    val link: String="",
    val timeStamp: Long=0,
    val userId: String="",
    val message:String=""
)
