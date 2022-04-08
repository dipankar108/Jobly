package com.university_project.jobly.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.university_project.jobly.R
import com.university_project.jobly.chatserver.ChatClickService
import com.university_project.jobly.chatserver.ChatListViewDataModel

class ChatListViewAdapter(private val listener:ChatClickService) : RecyclerView.Adapter<ChatListViewAdapter.ChatListViewHolder>() {
    private var chatlist = listOf<ChatListViewDataModel>()

    class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myName: TextView = itemView.findViewById(R.id.tv_chatListName_id)
        val profileImg: ImageView = itemView.findViewById(R.id.img_chatListProfile_id)
        val desc: TextView = itemView.findViewById(R.id.tv_chatListDesc_id)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.ll_chatItemView_id)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chatitemview, parent, false)
        return ChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val res = chatlist[position]
        holder.myName.text = res.empName
        holder.desc.text = "New Message here"
        holder.linearLayout.setOnClickListener {
            listener.onClickChat(res.docId)
        }
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