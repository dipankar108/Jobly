package com.university_project.jobly.datamodel

data class EmployeeProfileModel(
    val userId: String = "",
    val fname: String = "",
    val lname: String = "",
    val userEmail: String = "",
    val userPass: String = "",
    val userType: String = "",
    val skill: ArrayList<String> = ArrayList(),
    val currentCompany: String = "",
    val hobbyEmp: String = "",
    val aboutYourself: String = "",
    val verify: Boolean = false,
    val banned: Boolean = false,
    val cvEmp: String = "No CV",
    val profileImg: String = "",
    val active: Boolean = true,
    val company: ArrayList<String> = ArrayList()
)