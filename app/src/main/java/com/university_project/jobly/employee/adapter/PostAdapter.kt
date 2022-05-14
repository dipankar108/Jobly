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
import com.university_project.jobly.interfaces.ClickHandle
import com.university_project.jobly.utils.TimeStampConverter

class PostAdapter(private val listener: ClickHandle) :
    RecyclerView.Adapter<PostAdapter.EmpJobPostViewHolder>() {
    private var jobPostList = listOf<PostDataModel>()

    class EmpJobPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_postViewTitle_id)
        val desc: TextView = itemView.findViewById(R.id.tv_postViewDesc_id)
        val like: ImageView = itemView.findViewById(R.id.img_postViewliked_id)
        val timeStamp: TextView = itemView.findViewById(R.id.tv_postViewTimeStamp_id)
        val likeNum: TextView = itemView.findViewById(R.id.tv_jobPostLikeNum_id)
        val companyName: TextView = itemView.findViewById(R.id.tv_JobPostCompanyName_id)
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
        holder.timeStamp.text = TimeStampConverter.getTimeAgo(res.timeStamp)
        holder.companyName.text = res.companyName
        holder.desc.setOnClickListener {
            listener.onDescClick(res.docId)
        }
        //setting up onClick listener on on like button
        holder.like.setOnClickListener {
            if (res.isLike.contains(Firebase.auth.uid)) {
                listener.onLikeClick(res, true)
            } else listener.onLikeClick(res, false)
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