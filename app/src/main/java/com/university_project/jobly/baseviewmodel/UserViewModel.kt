package com.university_project.jobly.baseviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val _userBanInfo = ProfileRep.getUserData()
    val userBanInfo: LiveData<Boolean> get() = _userBanInfo
}