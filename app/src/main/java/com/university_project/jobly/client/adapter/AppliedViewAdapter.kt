package com.university_project.jobly.client.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.university_project.jobly.R
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.client.interfaces.AppliedClickedHandle

class AppliedViewAdapter(private val listner: AppliedClickedHandle) :
    RecyclerView.Adapter<AppliedViewAdapter.AppliedViewHolder>() {
    private var appliedArrayList = listOf<PostDataModel>()

    class AppliedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appliedTitle: TextView = itemView.findViewById(R.id.tv_clientAppliedTitle_id)
        val appliedDesc: TextView = itemView.findViewById(R.id.tv_clientAppliedDesc_id)
        val appliedNumber: TextView = itemView.findViewById(R.id.tv_clientAppliedNumber_id)
        val appliedLinearLayout: LinearLayout = itemView.findViewById(R.id.ll_appliedItemList_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.applied_items_view, parent, false)
        return AppliedViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedViewHolder, position: Int) {
        val res = appliedArrayList[position]
        holder.appliedTitle.text = res.title
        holder.appliedDesc.text = res.desc
        holder.appliedNumber.text = res.appliedEmployee.size.toString()
        holder.appliedLinearLayout.setOnClickListener {
            listner.onAppliedClicked(res)
        }
    }

    fun setArrayList(arrayList: List<PostDataModel>) {
        appliedArrayList = arrayList
    }

    override fun getItemCount(): Int {
        return appliedArrayList.size
    }
}