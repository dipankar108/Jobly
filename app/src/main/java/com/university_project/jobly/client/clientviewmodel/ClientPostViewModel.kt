package com.university_project.jobly.client.clientviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.client.datamodel.ClientPostDataModel

class ClientPostViewModel : ViewModel() {
    private var _postList = PostRepo.getResponseUsingLiveData()
    val postList: LiveData<List<ClientPostDataModel>> get() = _postList
    fun loading(): Boolean {
        if (_postList.value == null) {
            return true
        }
        return false
    }
}


