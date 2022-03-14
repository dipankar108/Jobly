package com.university_project.jobly.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.university_project.jobly.R
import com.university_project.jobly.chatserver.ChatDataModel
import com.university_project.jobly.chatserver.MessageModel

class MessageViewAdapter() : RecyclerView.Adapter<MessageViewAdapter.ChatadapterViewModel>() {
    private var messageProperty = ChatDataModel()
    private var messageList = listOf<MessageModel>()
    private var userType = ""

    class ChatadapterViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg: ImageView = itemView.findViewById(R.id.img_messageProfileImg_id)
        val normalImg: ImageView = itemView.findViewById(R.id.img_messageImg_id)
        val profileName: TextView = itemView.findViewById(R.id.tv_messageName_id)
        val message: TextView = itemView.findViewById(R.id.tv_messageDesc_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatadapterViewModel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.messageview, parent, false)
        return ChatadapterViewModel(view)
    }

    override fun onBindViewHolder(holder: ChatadapterViewModel, position: Int) {
        val res = messageList[position]
        if (res.link == "No Image") {
            holder.normalImg.visibility = View.GONE
        } else {
        }
        when {
            userType == res.userType -> {
                holder.profileName.text = "Me"
            }
            res.userType == "Client" -> {
                holder.profileName.text = messageProperty.cltName
            }
            else -> {
                holder.profileName.text = messageProperty.empName
            }
        }
        holder.message.text = res.message
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun setMessage(myMessage: ChatDataModel, userType: String?) {
        messageProperty = myMessage
        messageList = myMessage.messages
        this.userType = userType!!
    }
}