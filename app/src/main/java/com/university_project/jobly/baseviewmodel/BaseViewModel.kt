package com.university_project.jobly.baseviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.datamodel.CreatePostModel
import com.university_project.jobly.datamodel.EmployeeProfileModel

class BaseViewModel : ViewModel() {
    fun getSkill(): LiveData<List<String>> {
        return Repository.getSkill()
    }

    fun makePost(createPostModel: CreatePostModel) {
        Repository.createJobPost(createPostModel)
    }

    fun getEmployeeProfile(): LiveData<EmployeeProfileModel> {
        return Repository.getEmpProfile()
    }
}