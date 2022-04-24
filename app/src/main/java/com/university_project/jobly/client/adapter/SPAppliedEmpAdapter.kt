package com.university_project.jobly.client.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.university_project.jobly.R
import com.university_project.jobly.client.interfaces.SPAppliedEmpClick
import com.university_project.jobly.datamodel.AppliedDataModel

class SPAppliedEmpAdapter(private val listner: SPAppliedEmpClick) :
    RecyclerView.Adapter<SPAppliedEmpAdapter.AppliedEmployeeViewHolder>() {
    private var mlist = listOf<AppliedDataModel>()

    class AppliedEmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_appliedEmployeeTitle_id)
        val profileImage: ImageView = itemView.findViewById(R.id.iv_appliedEmployee_id)
        val btnAccept: ImageButton = itemView.findViewById(R.id.btn_appliedEmployeeAccept_id)
        val btnReject: ImageButton = itemView.findViewById(R.id.btn_appliedEmployeeReject_id)
        val btnDown: ImageButton = itemView.findViewById(R.id.btn_appliedEmployeeDown_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedEmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.applied_employee_item_view, parent, false)
        return AppliedEmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedEmployeeViewHolder, position: Int) {
        val res = mlist[position]
        holder.title.text = res.fullName
        Glide.with(holder.itemView.context)
            .load(res.profileImage)
            .placeholder(R.drawable.image_loding_anim)
            .error(R.drawable.ic_profileimg)
            .into(holder.profileImage)

        if (res.alreadyAdded) {
            holder.btnAccept.isEnabled = false
        }
        holder.btnAccept.setOnClickListener {
            listner.onAcceptEmp(res)
        }
        holder.btnDown.setOnClickListener {
            listner.onDownloadEmp(res.cvAttachment)
        }
    }

    fun setData(post: List<AppliedDataModel>) {
        this.mlist = post
    }

    override fun getItemCount(): Int {
        return mlist.size
    }
}