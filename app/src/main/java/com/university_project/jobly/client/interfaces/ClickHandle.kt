package com.university_project.jobly.client.interfaces

import com.university_project.jobly.datamodel.PostDataModel

interface ClickHandle {
    fun itemClicked(id:String, postDataModel: PostDataModel)
}