package com.university_project.jobly.datamodel

import com.university_project.jobly.datamodel.CallForInterViewDataModel

data class PostDataModel(
    val userId: String = "",
    val title: String = "",
    val desc: String = "",
    val category: List<String> = listOf(),
    val experience: Int = 0,
    val Salary: Int = 0,
    val location: String = "",
    val appliedEmployee: Map<String, String> = emptyMap(),
    val attachment: String = "",
    val timeStamp: Long = 0,
    val companyName: String = "",
    val genderName: String = "",
    val docId: String = "",
    val call_for_interview: ArrayList<CallForInterViewDataModel> = arrayListOf(),
    val isLike: ArrayList<String> = arrayListOf()
)
