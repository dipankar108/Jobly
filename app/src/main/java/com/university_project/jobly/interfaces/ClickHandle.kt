package com.university_project.jobly.interfaces

import com.university_project.jobly.datamodel.PostDataModel

interface ClickHandle {
    fun onLikeClick(postDataModel: PostDataModel, b: Boolean)
    fun onDescClick(docId:String)
    abstract fun onDeleteClick(docId: String)
}