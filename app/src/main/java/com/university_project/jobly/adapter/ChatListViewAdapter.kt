package com.university_project.jobly.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.university_project.jobly.R
import com.university_project.jobly.chatserver.ChatClickService
import com.university_project.jobly.chatserver.ChatDataModel

class ChatListViewAdapter(private val listener: ChatClickService, private val context: Context) :
    RecyclerView.Adapter<ChatListViewAdapter.ChatListViewHolder>() {
    private var chatlist = listOf<ChatDataModel>()
    private var userType = ""

    class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myName: TextView = itemView.findViewById(R.id.tv_chatListName_id)
        val desc: TextView = itemView.findViewById(R.id.tv_chatListDesc_id)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.ll_chatItemView_id)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chatitemview, parent, false)
        return ChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val res = chatlist[position]
        Log.d("FragTAG", "onViewCreated: ${res.lastClientMessage}")
        if (userType == "Client") {
            Log.d("FragTAG", "onViewCreated: ${res.empName}")
            val profileImg: ImageView = holder.itemView.findViewById(R.id.img_chatListProfile_id)
            holder.desc.text = res.lastEmpMessage
            holder.myName.text = res.empName
            Glide.with(context)
                .load(res.empProfileImg)
                .placeholder(R.drawable.image_loding_anim)
                .error(R.drawable.ic_profileimg)
                .into(profileImg)
        } else {
            val profileImg: ImageView = holder.itemView.findViewById(R.id.img_chatListProfile_id)
            Log.d("FragTAG", "onViewCreated: ${res.cltName}")
            holder.desc.text = res.lastClientMessage
            holder.myName.text = res.cltName
            Glide.with(context)
                .load(res.clientProfileImg)
                .placeholder(R.drawable.image_loding_anim)
                .error(R.drawable.ic_profileimg)
                .into(profileImg)
        }

        holder.linearLayout.setOnClickListener {
            listener.onClickChat(res.docId)
        }
    }

    override fun getItemCount(): Int {
        return chatlist.size
    }

    fun setChatList(chatlist: List<ChatDataModel>?, userType: String) {
        this.userType = userType
        if (chatlist != null) {
            this.chatlist = chatlist
        }
    }
}