package com.university_project.jobly.baseviewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.datamodel.CreatePostModel
import com.university_project.jobly.datamodel.EmployeeProfileModel
import com.university_project.jobly.datamodel.PostDataModel

class BaseViewModel : ViewModel() {
    fun getSkill(): LiveData<List<String>> {
        return Repository.getSkill()
    }

    fun makePost(createPostModel: CreatePostModel): LiveData<Boolean> {
        return Repository.createJobPost(createPostModel)
    }

    fun getEmployeeProfile(): LiveData<EmployeeProfileModel> {
        return Repository.getEmpProfile()
    }

    fun updateEmpProfile(employeeProfileModel: EmployeeProfileModel) {
        Repository.updateEmployeeProfile(employeeProfileModel)
    }

    fun getMyApplication(): LiveData<List<PostDataModel>> {
        return Repository.getEmpAppliedPost()
    }

    fun uploadCV(uri: Uri): LiveData<List<String>> {
        return Repository.uploadCV(uri)
    }
    fun isUserActive(userId:String):LiveData<Boolean> =Repository.isUserActive(userId)
}