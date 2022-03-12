package com.university_project.jobly.client.interfaces

import com.university_project.jobly.datamodel.AppliedDataModel

interface SPAppliedEmpClick {
    fun onAcceptEmp(postID: AppliedDataModel)
}