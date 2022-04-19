package com.university_project.jobly.chatserver

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.datamodel.AppliedDataModel

class ChatViewModel() : ViewModel() {
    fun createChatDoc(chatDataModel: AppliedDataModel) {
        Repository.createChatDoc(chatDataModel)
    }

    fun getChatList(userType: String, userId: String): LiveData<List<ChatDataModel>> {
        return Repository.getChatList(userType, userId)
    }

    fun getMessage(docId: String): LiveData<ChatDataModel> {
        return Repository.getMessage(docId)
    }

    fun sendMessage(docId: String, messageModel: MessageModel) {
        Repository.sendMessage(docId, messageModel)
    }

    fun isUserActive(userId: String): LiveData<Boolean> = Repository.isUserActive(userId)
}