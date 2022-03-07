package com.university_project.jobly.client.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.university_project.jobly.R
import com.university_project.jobly.client.datamodel.ClientPostDataModel

class AppliedEmployeeAdapter() :RecyclerView.Adapter<AppliedEmployeeAdapter.AppliedEmployeeViewHolder>() {
    val mlist = ArrayList<ClientPostDataModel>()
    class AppliedEmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedEmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.applied_employee_item_view, parent, false)
        return AppliedEmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedEmployeeViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return mlist.size
    }
}