package com.university_project.jobly.baseviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.datamodel.CreatePostModel

class BaseViewModel : ViewModel() {
    fun getSkill(): LiveData<List<String>> {
        return Repository.getSkill()
    }
    fun makePost(createPostModel: CreatePostModel){
        Repository.createJobPost(createPostModel)
    }
}