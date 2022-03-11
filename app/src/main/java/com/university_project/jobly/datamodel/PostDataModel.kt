package com.university_project.jobly.datamodel

data class PostDataModel(
    val userId: String = "",
    val title: String = "",
    val desc: String = "",
    val skill: List<String> = listOf(),
    val experience: Int = 0,
    val Salary: Int = 0,
    val location: String = "",
    val appliedEmployee: ArrayList<AppliedDataModel> = ArrayList(),
    val attachment: String = "",
    val timeStamp: Long = 0,
    val companyName: String = "",
    val genderName: String = "",
    val docId: String = "",
    val call_for_interview: ArrayList<CallForInterViewDataModel> = arrayListOf(),
    val isLike: ArrayList<String> = arrayListOf()
)
