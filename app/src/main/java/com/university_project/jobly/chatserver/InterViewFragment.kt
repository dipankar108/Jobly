package com.university_project.jobly.chatserver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.adapter.ChatListViewAdapter
import com.university_project.jobly.databinding.FragmentInterViewBinding

class InterViewFragment : Fragment() {
    private val binding get() = _binding
    private lateinit var _binding: FragmentInterViewBinding
    private lateinit var liveData: ChatViewModel
    private val myAdapter = ChatListViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInterViewBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        liveData = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        binding.rvChatListViewId.layoutManager=LinearLayoutManager(requireContext())
        binding.rvChatListViewId.adapter=myAdapter
        liveData.getChatList("cltId", Firebase.auth.uid.toString()).observe(viewLifecycleOwner, {
            myAdapter.setChatList(it)
           myAdapter.notifyDataSetChanged()
        })
    }
}