package com.university_project.jobly.client.interfaces

import com.university_project.jobly.client.datamodel.ClientPostDataModel

interface AppliedClickedHandle {
    fun onAppliedClicked(clientPostDataModel: ClientPostDataModel)
}