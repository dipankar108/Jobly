package com.university_project.jobly.client

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R
import com.university_project.jobly.client.clientviewmodel.ClientPostDataModel
import com.university_project.jobly.client.clientviewmodel.ClientPostViewModel
import com.university_project.jobly.databinding.FragmentClientAppliedBinding
import com.university_project.jobly.databinding.FragmentClientJobPostBinding
import com.university_project.jobly.datamodel.CreatePostModel

class ClientJobPostFragment : Fragment() {
    private  lateinit var _binding:FragmentClientJobPostBinding
    private lateinit var liveDataModel: ClientPostViewModel
    private lateinit var auth:FirebaseAuth
    private val binding get()=_binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        liveDataModel= ViewModelProvider(this)[ClientPostViewModel::class.java]
        _binding=FragmentClientJobPostBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth=Firebase.auth

        liveDataModel.getLiveData().observe(viewLifecycleOwner,{
            Log.d("TAG", "onViewCreated: ${liveDataModel.getLiveData().value}")
        })
    }
}

//{
//        val db=Firebase.firestore.collection("JobPost")
//        val query=db.whereEqualTo("userId",auth.uid.toString())
//        query.get().addOnSuccessListener {
//            for (doc in it.documents){
//                //Log.d("TAG", "onViewCreated: $doc")
//                val createPostModel=doc.toObject(ClientPostDataModel::class.java)
//                Log.d("TAG", "onViewCreated: ${createPostModel?.desc}")
//            }
//        }
//}