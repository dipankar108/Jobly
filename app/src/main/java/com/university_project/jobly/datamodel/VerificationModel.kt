package com.university_project.jobly.datamodel

data class VerificationModel(
    val pdfUrl: String,
    val userId: String,
    val timeStamp: Long,
    val userType: String
)