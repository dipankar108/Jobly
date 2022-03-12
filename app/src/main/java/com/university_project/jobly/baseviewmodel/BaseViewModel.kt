package com.university_project.jobly.baseviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.datamodel.AppliedDataModel
import com.university_project.jobly.datamodel.CreatePostModel
import com.university_project.jobly.datamodel.EmployeeProfileModel
import com.university_project.jobly.datamodel.PostDataModel

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

    fun updateEmpProfile(employeeProfileModel: EmployeeProfileModel) {
        Repository.updateEmployeeProfile(employeeProfileModel)
    }
    fun getMyApplication():LiveData<List<PostDataModel>>{
        return Repository.getEmpAppliedPost()
    }
}