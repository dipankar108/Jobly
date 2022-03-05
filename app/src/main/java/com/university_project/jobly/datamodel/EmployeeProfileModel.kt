package com.university_project.jobly.datamodel

data class EmployeeProfileModel(
    val userId: String,
    val fName: String,
    val lName: String,
    val userPass: String,
    val userEmail: String,
    val userType: String,
    val companyName: String,
    val appliedList:Map<String,String>,
    val calledForInterview:ArrayList<CallForInterViewDataModel>,
    val hobbyClient: String,
    val aboutYourself: String,
    val verify: Boolean,
    val banned: Boolean
)