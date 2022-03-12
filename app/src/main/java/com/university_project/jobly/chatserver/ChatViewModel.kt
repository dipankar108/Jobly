package com.university_project.jobly.chatserver

import androidx.lifecycle.ViewModel
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.datamodel.AppliedDataModel

class ChatViewModel():ViewModel() {
    fun createChatDoc(chatDataModel: AppliedDataModel){
        Repository.createChatDoc(chatDataModel)
    }

}