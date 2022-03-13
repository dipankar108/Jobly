package com.university_project.jobly.chatserver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R
import com.university_project.jobly.databinding.ActivityChatBinding
import com.university_project.jobly.utils.GetTheme

class ChatActivity : AppCompatActivity() {
    private lateinit var liveData: ChatViewModel
    private lateinit var binding: ActivityChatBinding
    private val myAdapter = ChatAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        if (GetTheme.getDarkTheme(resources)) setTheme(R.style.Theme_SplashJoblyDarkNoActionBar)
        else setTheme(R.style.Theme_SplashJoblyLightNoActoinBar)
        setContentView(binding.root)
        binding.rvViewMessageListId.layoutManager = LinearLayoutManager(this)
        binding.rvViewMessageListId.adapter = myAdapter
        liveData = ViewModelProvider(this)[ChatViewModel::class.java]
        liveData.getChatList("cltId", Firebase.auth.uid.toString()).observe(this, {
            myAdapter.setMessage(it)
            myAdapter.notifyDataSetChanged()
        })
        binding.imgMessageSendId.setOnClickListener {

        }
    }
}