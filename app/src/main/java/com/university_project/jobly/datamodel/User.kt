package com.university_project.jobly.datamodel

data class User(
    val userId: String,
    val fName: String,
    val lName: String,
    val userPass: String,
    val userEmail: String,
    val userType: String
)
