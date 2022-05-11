package com.university_project.jobly.baseviewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university_project.jobly.datamodel.*

class BaseViewModel : ViewModel() {
    fun getSkill(): LiveData<List<String>> {
        return Repository.getSkill()
    }

    fun makePost(
        postTitle: String,
        postDesc: String,
        postExperience: Int,
        postSkills: MutableList<String>,
        postSalary: Int,
        postGender: String,
        isLike: ArrayList<String>,
        callforinterview: ArrayList<CallForInterViewDataModel>,
        timeStamp: Long,
        appliedEmployee: ArrayList<AppliedDataModel>,
        attachmentLink: Uri
    ): LiveData<Boolean> {
        return Repository.createJobPost(
            postTitle,
            postDesc,
            postExperience,
            postSkills,
            postSalary,
            postGender,
            isLike,
            callforinterview,
            timeStamp,
            appliedEmployee,
            attachmentLink
        )
    }

    fun getEmployeeProfile(): LiveData<EmployeeProfileModel> = Repository.getEmpProfile()
    fun getClientProfile(): LiveData<ClientProfileModel> = Repository.getClientProfile()
    fun getMyApplication(): LiveData<List<PostDataModel>> = Repository.getEmpAppliedPost()
    fun uploadCV(uri: Uri): LiveData<List<String>> = Repository.uploadCV(uri)
    fun isUserActive(userId: String): LiveData<Boolean> = Repository.isUserActive(userId)
    fun getCompany(): LiveData<List<String>> = Repository.getCompany()
    fun isVerified(userType: String): LiveData<Boolean> = Repository.isVerified(userType)
    fun setVerificationFile(pdfUri: Uri, userType: String) {
        Repository.setVerfication(pdfUri, userType)
    }
}