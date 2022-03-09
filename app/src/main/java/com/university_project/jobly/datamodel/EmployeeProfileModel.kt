package com.university_project.jobly.datamodel

data class EmployeeProfileModel(
    val userId: String,
    val fName: String,
    val lName: String,
    val userEmail: String,
    val userPass: String,
    val userType: String,
    val skill:ArrayList<String>,
    val currentCompany: String,
    val hobbyEmp: String,
    val aboutYourself: String,
    val verify: Boolean,
    val banned: Boolean
)