package com.university_project.jobly.chatserver

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.sourceInformationMarkerEnd
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.university_project.jobly.R
import com.university_project.jobly.adapter.MessageViewAdapter
import com.university_project.jobly.databinding.ActivityChatBinding
import com.university_project.jobly.utils.GetTheme
import com.university_project.jobly.utils.SharedInfo

class ChatActivity : AppCompatActivity() {
    private lateinit var liveData: ChatViewModel
    private lateinit var binding: ActivityChatBinding
    private val myAdapter = MessageViewAdapter()
    private val storage = Firebase.storage
    private var storageRef = storage.reference
    private lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        if (GetTheme.getDarkTheme(resources)) setTheme(R.style.Theme_SplashJoblyDarkNoActionBar)
        else setTheme(R.style.Theme_SplashJoblyLightNoActoinBar)
        setContentView(binding.root)
        val docId = intent.getStringExtra("docId")
        binding.rvViewMessageListId.layoutManager = LinearLayoutManager(this)
        binding.rvViewMessageListId.adapter = myAdapter
        liveData = ViewModelProvider(this)[ChatViewModel::class.java]
        val userType = getSharedPreferences(
            SharedInfo.USER.user,
            MODE_PRIVATE
        ).getString(SharedInfo.USER_TYPE.user, null)!!
        liveData.getMessage(docId!!).observe(this, {
            myAdapter.setMessage(it, userType)
            myAdapter.notifyDataSetChanged()
            if (it.messages.size > 0) {
                binding.rvViewMessageListId.smoothScrollToPosition(it.messages.size - 1)
            }
        })
        var uploadImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { imageRes: ActivityResult ->
                if (imageRes.resultCode == Activity.RESULT_OK) {
                    imageUri = Uri.parse(imageRes.data?.data.toString())
                    binding.imgSendImgId.setImageURI(imageUri)
                    binding.llUploadImageId.visibility=View.VISIBLE
                }
            }
        binding.imgSelectImageId.setOnClickListener {
            val intent = Intent()
            intent.type = ("image/*")
            intent.action=Intent.ACTION_GET_CONTENT
            uploadImage.launch(intent)
        }
        binding.imdBtnDeleteImgId.setOnClickListener {
            imageUri=Uri.EMPTY
            binding.imgSendImgId.setImageURI(imageUri)
            binding.llUploadImageId.visibility=View.GONE
        }
        binding.imgMessageSendId.setOnClickListener {
            val link = "No Image"
            val timeStamp = System.currentTimeMillis()
            val userId = Firebase.auth.uid.toString()
            val message = binding.etEnterMessageId.text.toString()
            if (message.isNotEmpty()) {
                binding.etEnterMessageId.text?.clear()
                liveData.sendMessage(
                    docId,
                    MessageModel(
                        link,
                        timeStamp,
                        userId,
                        message,
                        userType
                    )
                )
            }

        }
    }
}