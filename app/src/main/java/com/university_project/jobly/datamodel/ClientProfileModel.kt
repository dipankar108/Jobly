package com.university_project.jobly.datamodel

data class ClientProfileModel(
    val userId: String,
    val fName: String,
    val lName: String,
    val userPass: String,
    val userEmail: String,
    val userType: String,
    val companyName: String,
    val totalPost: ArrayList<String>,
    val hobbyClient: String,
    val aboutYourself: String,
    val verify: Boolean,
    val banned: Boolean
)
