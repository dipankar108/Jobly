package com.university_project.jobly.client.datamodel

import com.university_project.jobly.datamodel.AppliedDataModel
import com.university_project.jobly.datamodel.CallForInterViewDataModel

data class AppliedEmployeeDataModel(
    val userId: String = "",
    val appliedEmployee: ArrayList<AppliedDataModel> = ArrayList(),
    val docId: String = "",
    val call_for_interview: ArrayList<CallForInterViewDataModel>
)
