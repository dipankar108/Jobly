package com.university_project.jobly.client.clientviewmodel

data class ClientPostDataModel (
    val userId:String="",
    val title:String="",
    val desc:String="",
    val category: String="",
    val experience:Int=0,
    val Salary:Int=0,
    val location:String="",
    val appliedEmployee: Map<String, String> = emptyMap<String,String>(),
    val attachment:String="",
    val timeStamp:Long=0,
    val companyName:String="",
    val genderName:String=""
)
