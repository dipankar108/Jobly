package com.university_project.jobly.baseviewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val _userBanInfo = ProfileRep.getUserData()
    val userBanInfo: LiveData<Boolean> get() = _userBanInfo
    fun updateProfile(value: String, fieldName: String) {
        ProfileRep.updateProfile(value, fieldName)
    }

    fun updateChatImage(url: String, fieldName: String, updateFieldName: String) {
        ProfileRep.updateChatImage(url, fieldName, updateFieldName)
    }
}