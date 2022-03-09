package com.university_project.jobly.employee

import com.university_project.jobly.datamodel.PostDataModel

interface ClickHandle {
    fun onLikeClick(postDataModel: PostDataModel, b: Boolean)
}