package com.university_project.jobly.client.clientviewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClientPostViewModel: ViewModel() {
    private  var getData=MutableLiveData<ArrayList<ClientPostDataModel>>()
    fun getLiveData():MutableLiveData<ArrayList<ClientPostDataModel>>{
       getData=PostRepo.getJobPost() as MutableLiveData<ArrayList<ClientPostDataModel>>
        return getData
    }
    fun init(){
//        if (getData.value!=null){
//            return
//        }
        PostRepo.setJobPost()
    }
}