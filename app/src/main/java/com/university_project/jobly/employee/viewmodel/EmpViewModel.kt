package com.university_project.jobly.employee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.datamodel.PostDataModel

class EmpViewModel : ViewModel() {
    fun getJobPost(skill: List<String>): LiveData<List<PostDataModel>> {
        return Repository.getPostFromDataBase(skill)
    }
}