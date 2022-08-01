package com.university_project.jobly.verify

data class ModelVerify(
    var name: String = "",
    var id: String = "",
    var dob: String = "",
    var registerId: String = "",
    var alreadyExists: Boolean = false
)