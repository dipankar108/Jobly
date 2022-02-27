package com.university_project.jobly.client.clientviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.datamodel.CreatePostModel

object PostRepo {
    private val jobPostList= mutableSetOf<ClientPostDataModel>()
    fun getJobPost():LiveData<ArrayList<ClientPostDataModel>> {
        setJobPost()
        val mutableList=MutableLiveData<ArrayList<ClientPostDataModel>>()
        mutableList.value= ArrayList(jobPostList)
        return mutableList
    }
    fun setJobPost(){
       val auth=Firebase.auth
       val db=Firebase.firestore.collection("JobPost")
       val query=db.whereEqualTo("userId",auth.uid.toString())
       query.get().addOnSuccessListener {
           for (doc in it.documents){
              val clientjobpostdatamodel= doc.toObject(ClientPostDataModel::class.java)
               if (clientjobpostdatamodel != null) {
                   jobPostList.add(clientjobpostdatamodel)
               }
           }
       }
   }
}