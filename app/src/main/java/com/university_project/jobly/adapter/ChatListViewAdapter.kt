package com.university_project.jobly.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.university_project.jobly.R
import com.university_project.jobly.chatserver.ChatListViewDataModel

class ChatListViewAdapter() : RecyclerView.Adapter<ChatListViewAdapter.ChatListViewHolder>() {
    private var chatlist = listOf<ChatListViewDataModel>()

    class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myName: TextView = itemView.findViewById(R.id.tv_chatListName_id)
        val profileImg: ImageView = itemView.findViewById(R.id.img_chatListProfile_id)
        val desc: TextView = itemView.findViewById(R.id.tv_chatListDesc_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chatitemview, parent, false)
        return ChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val res = chatlist[position]
        holder.myName.text = res.empName
        holder.desc.text = "New Message here"

    }

    override fun getItemCount(): Int {
        return chatlist.size
    }

    fun setChatList(chatlist: List<ChatListViewDataModel>?) {
        if (chatlist != null) {
            this.chatlist = chatlist
        }
    }
}