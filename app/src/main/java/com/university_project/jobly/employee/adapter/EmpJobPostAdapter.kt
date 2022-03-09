package com.university_project.jobly.employee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.employee.ClickHandle
import com.university_project.jobly.utils.TimeStampConverter
import com.university_project.jobly.utils.UtilClass

class EmpJobPostAdapter(private val likeClick: ClickHandle) :
    RecyclerView.Adapter<EmpJobPostAdapter.EmpJobPostViewHolder>() {
    private var jobPostList = listOf<PostDataModel>()

    class EmpJobPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_postViewTitle_id)
        val desc: TextView = itemView.findViewById(R.id.tv_postViewDesc_id)
        val like: ImageView = itemView.findViewById(R.id.img_postViewliked_id)
        val position: TextView = itemView.findViewById(R.id.tv_postViewPosition_id)
        val timeStamp: TextView = itemView.findViewById(R.id.tv_postViewTimeStamp_id)
        val likeNum: TextView = itemView.findViewById(R.id.tv_jobPostLikeNum_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpJobPostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.postitemview, parent, false)
        return EmpJobPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmpJobPostViewHolder, position: Int) {
        val res = jobPostList[position]
        holder.title.text = res.title
        holder.desc.text = res.desc
        holder.likeNum.text = res.isLike.size.toString()
        holder.position.text=res.category[0]
        holder.timeStamp.text=TimeStampConverter.getTimeAgo(res.timeStamp)
            //setting up onClick listener on on like button
        holder.like.setOnClickListener {
            if (res.isLike.contains(Firebase.auth.uid)) {
                likeClick.onLikeClick(res, true)
            } else likeClick.onLikeClick(res, false)
        }
        if (res.isLike.contains(Firebase.auth.uid)) {
            holder.like.setImageResource(R.drawable.ic_like)
        } else {
            holder.like.setImageResource(R.drawable.ic_unlike)
        }
    }

    override fun getItemCount(): Int {
        return jobPostList.size
    }

    fun setDataToList(jobPostList: List<PostDataModel>) {
        this.jobPostList = jobPostList
    }
}