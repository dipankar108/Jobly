package com.university_project.jobly.client.clientviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.datamodel.AppliedDataModel
import com.university_project.jobly.datamodel.PostDataModel

class ClientPostViewModel : ViewModel() {
    private var _postList = Repository.getCltPost()
    val postList: LiveData<List<PostDataModel>> get() = _postList
    fun loading(): Boolean {
        if (_postList.value == null) {
            return true
        }
        return false
    }

    fun deletePost(docId: String) {
        Repository.deletePost(docId)
    }

    fun getAppliedEmployee(docId: String): LiveData<List<AppliedDataModel>> {
        return Repository.getAppliedEmployeeList(docId)
    }

    fun getSinglePost(docId: String): LiveData<PostDataModel> {
        return Repository.singlePost(docId)
    }

    fun appliedForPost(docID: String): LiveData<String> {
        return Repository.applyForPost(docID)
    }
}


