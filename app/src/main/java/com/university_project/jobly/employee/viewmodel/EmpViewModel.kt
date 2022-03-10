package com.university_project.jobly.employee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.baseviewmodel.post_repository.Repository
import com.university_project.jobly.datamodel.PostDataModel

class EmpViewModel : ViewModel() {
    fun getJobPost(skill: List<String>): LiveData<List<PostDataModel>> {
        return Repository.getEmpPost(skill)
    }

    fun updateLike(docId: String, userId: String, b: Boolean) {
        Repository.updateLike(docId, userId,b)
    }
    fun getAllFabPost():LiveData<List<PostDataModel>>{
      return Repository.getFabPost()
    }
}