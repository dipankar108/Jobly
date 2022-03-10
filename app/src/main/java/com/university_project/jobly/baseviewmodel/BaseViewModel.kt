package com.university_project.jobly.baseviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class BaseViewModel : ViewModel() {
    fun getSkill(): LiveData<List<String>> {
        return Repository.getSkill()
    }
}