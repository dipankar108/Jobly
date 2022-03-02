package com.university_project.jobly.client.clientviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ClientPostViewModel : ViewModel() {
    private var _postList = MutableLiveData<ArrayList<ClientPostDataModel>>()
    val postList: LiveData<ArrayList<ClientPostDataModel>> get() = _postList

   fun loading(): Boolean {
        if (_postList.value == null) {
            return true
        }
        return false
    }

    fun getLiveData(): MutableLiveData<ArrayList<ClientPostDataModel>> {
        Log.d("TAG", "getLiveData: " + _postList.value)

        if (_postList.value != null) {
            return _postList
        } else {
            viewModelScope.launch {
                Log.i("TAG", "getLiveData: viewmodelscoped called")
                _postList.postValue(PostRepo.getJobPost())
            }
        }
        return _postList
    }

//    suspend fun init() {
//        if (getData.value != null) {
//            return
//        }
//        PostRepo.setJobPost()
//    }
}

