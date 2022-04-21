package com.university_project.jobly.datamodel

data class ClientProfileModel(
    val userId: String = "",
    val fName: String = "",
    val lName: String = "",
    val userPass: String = "",
    val userEmail: String = "",
    val userType: String = "",
    val companyName: String = "",
    val companyLocation: String = "",
    val totalPost: ArrayList<String> = arrayListOf(),
    val hobbyClient: String = "",
    val aboutYourself: String = "",
    val verify: Boolean = false,
    val banned: Boolean = false,
    val isActive: Boolean = false,
    val profileImg: String = ""
)
