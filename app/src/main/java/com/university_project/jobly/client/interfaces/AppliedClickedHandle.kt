package com.university_project.jobly.client.interfaces

import com.university_project.jobly.datamodel.PostDataModel

interface AppliedClickedHandle {
    fun onAppliedClicked(postDataModel: PostDataModel)
}