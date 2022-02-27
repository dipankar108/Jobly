package com.university_project.jobly.datamodel

data class CProfile(
    val userId: String,
    val fName: String,
    val lName: String,
    val totalPost: ArrayList<String>
)