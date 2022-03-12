package com.university_project.jobly.datamodel

data class CreatePostModel(
    val userId: String,
    val title: String,
    val desc: String,
    val skill: List<String>,
    val experience: Int,
    val Salary: Int,
    val location: String,
    val appliedEmployee: ArrayList<AppliedDataModel>,
    val employeeId: ArrayList<String> = ArrayList(),
    val attachment: String,
    val timeStamp: Long,
    val companyName: String,
    val genderName: String,
    val call_for_interview: ArrayList<CallForInterViewDataModel> = ArrayList(),
    val isLike: ArrayList<String> = arrayListOf()
)
