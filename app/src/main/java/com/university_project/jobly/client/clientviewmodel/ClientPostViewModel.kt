package com.university_project.jobly.client.clientviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.client.datamodel.AppliedEmployeeDataModel
import com.university_project.jobly.datamodel.PostDataModel

class ClientPostViewModel : ViewModel() {
    private var _postList = PostRepo.getResponseUsingLiveData()
    val postList: LiveData<List<PostDataModel>> get() = _postList
    fun loading(): Boolean {
        if (_postList.value == null) {
            return true
        }
        return false
    }

    fun getAppliedEmployee(docId: String): LiveData<List<AppliedEmployeeDataModel>> {
        return PostRepo.getAppliedEmployeeList(docId)
    }
}


