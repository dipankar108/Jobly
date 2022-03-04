package com.university_project.jobly.client.interfaces

import com.university_project.jobly.client.datamodel.ClientPostDataModel

interface ClickHandle {
    fun itemClicked(id:String,clientPostDataModel: ClientPostDataModel)
}