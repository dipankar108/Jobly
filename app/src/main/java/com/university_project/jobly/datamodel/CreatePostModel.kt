package com.university_project.jobly.datamodel

data class CreatePostModel(
    val userId: String,
    val title: String,
    val desc: String,
    val category: ArrayList<String>,
    val experience: Int,
    val Salary: Int,
    val location: String,
    val appliedEmployee: Map<String, String>,
    val attachment: String,
    val timeStamp: Long,
    val companyName: String,
    val genderName: String,
    val call_for_interview: ArrayList<CallForInterViewDataModel> = ArrayList()
)
