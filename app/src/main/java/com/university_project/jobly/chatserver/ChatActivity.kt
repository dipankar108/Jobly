package com.university_project.jobly.chatserver

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
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
    private lateinit var messangerName: TextView
    private lateinit var isMessangerActive: TextView
    private lateinit var messangerProfileCard: MaterialCardView
    private val myAdapter = MessageViewAdapter(this)
    private val storage = Firebase.storage
    private var imageUri = Uri.EMPTY

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        if (GetTheme.getDarkTheme(resources)) setTheme(R.style.Theme_SplashJoblyDarkNoActionBar)
        else setTheme(R.style.Theme_SplashJoblyLightNoActoinBar)
        setContentView(binding.root)
        val docId = intent.getStringExtra("docId")
        messangerName = findViewById(R.id.tv_messangerNameId)
        isMessangerActive = findViewById(R.id.tv_isMessangerActive_id)
        messangerProfileCard = findViewById(R.id.cv_messangerCard_id)
        binding.rvViewMessageListId.layoutManager = LinearLayoutManager(this)
        binding.rvViewMessageListId.adapter = myAdapter
        liveData = ViewModelProvider(this)[ChatViewModel::class.java]
        val userType = getSharedPreferences(
            SharedInfo.USER.user,
            MODE_PRIVATE
        ).getString(SharedInfo.USER_TYPE.user, null)!!
        liveData.getMessage(docId!!).observe(this) { userDetails ->
            if (userType == "Client") {
                liveData.isUserActive(userDetails.empId).observe(this) { isActive ->
                    myAdapter.setMessage(userDetails, userType, isActive)

                    if (userType != userDetails.messages[0].userType) {
                        messangerName.text = userDetails.empName
                        if (isActive) {
                            isMessangerActive.text = "Active Now"
                            messangerProfileCard.strokeColor = Color.GREEN
                        } else {
                            messangerProfileCard.strokeColor = Color.RED
                            isMessangerActive.text = "Inactive"
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                    binding.rvViewMessageListId.smoothScrollToPosition(userDetails.messages.size - 1)
                }
            } else {
                liveData.isUserActive(userDetails.cltId).observe(this) { isActive ->
                    myAdapter.setMessage(userDetails, userType, isActive)
                    if (userType != userDetails.messages[0].userType) {
                        messangerName.text = userDetails.cltName
                        if (isActive) {
                            isMessangerActive.text = "Active Now"
                            messangerProfileCard.strokeColor = Color.GREEN
                        } else {
                            messangerProfileCard.strokeColor = Color.RED
                            isMessangerActive.text = "Inactive"
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                    binding.rvViewMessageListId.smoothScrollToPosition(userDetails.messages.size - 1)
                }
            }
        }
        var uploadImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { imageRes: ActivityResult ->
                if (imageRes.resultCode == Activity.RESULT_OK) {
                    imageUri = Uri.parse(imageRes.data?.data.toString())
                    binding.imgSendImgId.setImageURI(imageUri)
                    binding.llUploadImageId.visibility = View.VISIBLE
                }
            }
        binding.imgSelectImageId.setOnClickListener {
            val intent = Intent()
            intent.type = ("image/*")
            intent.action = Intent.ACTION_GET_CONTENT
            uploadImage.launch(intent)
        }
        binding.imdBtnDeleteImgId.setOnClickListener {
            imageUri = Uri.EMPTY
            binding.imgSendImgId.setImageURI(imageUri)
            binding.llUploadImageId.visibility = View.GONE
        }
        binding.imgMessageSendId.setOnClickListener {
            binding.pbMessageImagePerId.visibility = View.VISIBLE
            val storageRef = storage.reference.child("message/image/${System.currentTimeMillis()}")
            var link = "No Image"
            val timeStamp = System.currentTimeMillis()
            val userId = Firebase.auth.uid.toString()
            val message = binding.etEnterMessageId.text.toString()
            if (!Uri.EMPTY.equals(imageUri)) {
                storageRef.putFile(imageUri).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        link = uri.toString()
                        sendMessage(message, docId, link, timeStamp, userId, userType)
                        binding.pbMessageImagePerId.visibility = View.GONE
                        binding.tvMessageProgressId.visibility = View.GONE
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }.addOnProgressListener {
                    binding.pbMessageImagePerId.visibility = View.VISIBLE
                    binding.tvMessageProgressId.visibility = View.VISIBLE
                    var uploadingPer = (100 * it.bytesTransferred / it.totalByteCount)
                    binding.pbMessageImagePerId.progress = uploadingPer.toInt()
                    binding.tvMessageProgressId.text = "$uploadingPer%"
                }
            } else {
                sendMessage(message, docId, link, timeStamp, userId, userType)
            }
        }
    }

    private fun sendMessage(
        message: String,
        docId: String,
        link: String,
        timeStamp: Long,
        userId: String,
        userType: String
    ) {
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
        imageUri = Uri.EMPTY
        binding.llUploadImageId.visibility = View.GONE
        binding.etEnterMessageId.text?.clear()
        binding.pbMessageImagePerId.visibility = View.GONE
    }
}