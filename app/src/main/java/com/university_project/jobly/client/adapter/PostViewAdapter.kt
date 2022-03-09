package com.university_project.jobly.client.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.university_project.jobly.R
import com.university_project.jobly.client.interfaces.ClickHandle
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.utils.TimeStampConverter

class PostViewAdapter(private val listener: ClickHandle) : RecyclerView.Adapter<PostViewAdapter.PostViewHolder>() {
    private var myArrayList =listOf<PostDataModel>()
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postTitle: TextView = itemView.findViewById(R.id.tv_postViewTitle_id)
        val postDesc: TextView = itemView.findViewById(R.id.tv_postViewDesc_id)
        val position: TextView = itemView.findViewById(R.id.tv_postViewPosition_id)
        val timeStamp: TextView = itemView.findViewById(R.id.tv_postViewExperiment_id)
        val location: TextView = itemView.findViewById(R.id.tv_postViewLocation_id)
    }

    fun setArrayList(arrayList: List<PostDataModel>) {
        myArrayList= arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.postitemview, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val res = myArrayList[position]
        holder.postTitle.text = res.title
        holder.postDesc.text = res.desc
        holder.position.text = res.category[0]
        holder.timeStamp.text = TimeStampConverter.getTimeAgo(res.timeStamp)
        holder.location.text = res.location
        holder.postDesc.setOnClickListener {
            listener.itemClicked(res.docId,res)
        }
    }
    override fun getItemCount(): Int {
        return myArrayList.size
    }

}