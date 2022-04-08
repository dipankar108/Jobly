package com.university_project.jobly.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.university_project.jobly.R
import com.university_project.jobly.chatserver.ChatDataModel
import com.university_project.jobly.chatserver.MessageModel
import com.university_project.jobly.utils.TimeStampConverter

class MessageViewAdapter(private val context: Context) : Adapter<RecyclerView.ViewHolder>() {
    private val ME_VIEW = 1
    private val OPPS_VIEW = 2
    private var messageProperty = ChatDataModel()
    private var messageList = listOf<MessageModel>()
    private var userType = ""
    private var isActive = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ME_VIEW) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.messageviewme, parent, false)
            MessageMeViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.messageviewoposite, parent, false)
            MessageOppViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val res = messageList[position]
        if (res.userType == userType) {
            val meHolder: MessageMeViewHolder = holder as MessageMeViewHolder
            setMeView(meHolder, res)
        } else {
            val oppHolder: MessageOppViewHolder = holder as MessageOppViewHolder
            setOppView(oppHolder, res, isActive)
        }
    }


    override fun getItemCount() = messageList.size

    override fun getItemViewType(position: Int) =
        if (messageList[position].userType == userType) {
            ME_VIEW
        } else OPPS_VIEW

    class MessageMeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val me_profileImg: ImageView = itemView.findViewById(R.id.img_messageProfileImgMe_id)
        val me_messageFull: TextView = itemView.findViewById(R.id.tv_messageMe_id)
        val me_messageTime: TextView = itemView.findViewById(R.id.tv_messageTimeMe_id)
        val me_messageImageView: ImageView = itemView.findViewById(R.id.img_messageImgMe_id)
        val me_imageCard: CardView = itemView.findViewById(R.id.cv_messageImageViewMe_id)
    }

    class MessageOppViewHolder(oppView: View) : RecyclerView.ViewHolder(oppView) {
        val opp_profileImg: ImageView = itemView.findViewById(R.id.iv_messangerProfile_id)
        val opp_messageFull: TextView = itemView.findViewById(R.id.tv_messageOpp_id)
        val opp_messageTime: TextView = itemView.findViewById(R.id.tv_messageTimeOpp_id)
        val opp_messageImageView: ImageView = itemView.findViewById(R.id.img_messageImgOpp_id)
        val opp_profileCard: MaterialCardView = itemView.findViewById(R.id.cv_profileImage_id)
        val opp_imageCard: CardView = itemView.findViewById(R.id.cv_messageImageViewOpp_id)
    }

    fun setMessage(myMessage: ChatDataModel, userType: String?, isActive: Boolean) {
        messageProperty = myMessage
        messageList = myMessage.messages
        this.userType = userType!!
        this.isActive = isActive
    }

    private fun setMeView(meHolder: MessageMeViewHolder, res: MessageModel) {
        meHolder.me_messageFull.text = res.message
        meHolder.me_messageTime.text = TimeStampConverter.getTimeAgo(res.timeStamp)
        if (res.link != "No Image") {
            Glide.with(context)
                .load(res.link)
                .placeholder(R.drawable.image_loding_anim)
                .error(R.drawable.try_later)
                .into(meHolder.me_messageImageView)
        } else meHolder.me_imageCard.visibility = View.GONE
        if (res.userType == "Client") {
            Glide.with(context)
                .load(messageProperty.clientProfileImg)
                .placeholder(R.drawable.image_loding_anim)
                .error(R.drawable.ic_profileimg)
                .into(meHolder.me_profileImg)
        } else {
            Glide.with(context)
                .load(messageProperty.empProfileImg)
                .placeholder(R.drawable.image_loding_anim)
                .error(R.drawable.ic_profileimg)
                .into(meHolder.me_profileImg)
        }
    }

    private fun setOppView(oppHolder: MessageOppViewHolder, res: MessageModel, isActive: Boolean) {
        oppHolder.opp_messageFull.text = res.message
        oppHolder.opp_messageTime.text = TimeStampConverter.getTimeAgo(res.timeStamp)

        if (!isActive) {
            oppHolder.opp_profileCard.strokeColor = Color.RED
        }else{
            oppHolder.opp_profileCard.strokeColor = Color.GREEN
        }
        if (res.link != "No Image") {
            Glide.with(context)
                .load(res.link)
                .placeholder(R.drawable.image_loding_anim)
                .error(R.drawable.try_later)
                .into(oppHolder.opp_messageImageView)
        } else oppHolder.opp_imageCard.visibility = View.GONE

        if (res.userType == "Client") {
            Glide.with(context)
                .load(messageProperty.clientProfileImg)
                .placeholder(R.drawable.image_loding_anim)
                .error(R.drawable.ic_profileimg)
                .into(oppHolder.opp_profileImg)
        } else {
            Glide.with(context)
                .load(messageProperty.empProfileImg)
                .placeholder(R.drawable.image_loding_anim)
                .error(R.drawable.ic_profileimg)
                .into(oppHolder.opp_profileImg)
        }
    }
}