package com.university_project.jobly.chatserver

import android.content.Context.MODE_PRIVATE
import android.content.Intent
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
import com.university_project.jobly.utils.SharedInfo

class InterViewFragment : Fragment(), ChatClickService {
    private val binding get() = _binding
    private lateinit var _binding: FragmentInterViewBinding
    private lateinit var liveData: ChatViewModel
    private lateinit var myAdapter: ChatListViewAdapter
    private var userType = ""
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
        myAdapter = context?.let { ChatListViewAdapter(this, it) }!!
        liveData = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        binding.rvChatListViewId.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChatListViewId.adapter = myAdapter
        val sh = activity?.getSharedPreferences(SharedInfo.USER.user, MODE_PRIVATE)
        userType = if (sh?.getString(SharedInfo.USER_TYPE.user, null) == "Client") {
            "cltId"
        } else "empId"
        liveData.getChatList(userType, Firebase.auth.uid.toString())
            .observe(viewLifecycleOwner) { list ->
              val sortedList=list.sortedByDescending { it.timeStamp }
                myAdapter.setChatList(sortedList,userType)
                sortedList.forEach {
                    Log.d("TAG", "onViewCreated: ${it.timeStamp} ${it.cltName}")
                }
                myAdapter.notifyDataSetChanged()
            }
    }

    override fun onClickChat(docId: String) {
        val intent = Intent(requireContext(), ChatActivity::class.java)
        intent.putExtra("docId", docId)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}