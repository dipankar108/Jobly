package com.university_project.jobly.client.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R
import com.university_project.jobly.chatserver.ChatViewModel

class ClientCallForInterViewFragment : Fragment() {
private lateinit var liveData:ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_call_for_inter_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        liveData= ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        liveData.getChatList("cltId",Firebase.auth.uid.toString()).observe(viewLifecycleOwner,{
            Log.d("TAG", "onViewCreated: ${it[0]}")
        })
    }
}