package com.university_project.jobly.chatserver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R

class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ChatadapterViewModel>() {
    private var messageProperty = listOf<ChatDataModel>()
    private var messageList = listOf<MessageModel>()

    class ChatadapterViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg: ImageView = itemView.findViewById(R.id.img_messageProfileImg_id)
        val normalImg: ImageView = itemView.findViewById(R.id.img_messageImg_id)
        val profileName: TextView = itemView.findViewById(R.id.tv_messageName_id)
        val message: TextView = itemView.findViewById(R.id.tv_messageDesc_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatadapterViewModel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chatitemview, parent, false)
        return ChatadapterViewModel(view)
    }

    override fun onBindViewHolder(holder: ChatadapterViewModel, position: Int) {
        val res = messageList[position]
        if (res.userId == Firebase.auth.uid.toString()) {
            holder.profileName.text = messageProperty[0].empName
        } else holder.profileName.text = "Me"
        holder.message.text = res.message
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun setMessage(myMessage: List<ChatDataModel>) {
        messageProperty = myMessage
        messageList = myMessage[0].messages
    }
}