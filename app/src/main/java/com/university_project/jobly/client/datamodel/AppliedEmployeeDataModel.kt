package com.university_project.jobly.client.datamodel

import com.university_project.jobly.datamodel.CallForInterViewDataModel

data class AppliedEmployeeDataModel(
    val userId: String = "",
    val appliedEmployee: Map<String, String> = emptyMap(),
    val docId: String = "",
    val call_for_interview: ArrayList<CallForInterViewDataModel>
)
