package com.university_project.jobly.employee.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.university_project.jobly.datamodel.PostDataModel

class EmpJobPostAdapter():RecyclerView.Adapter<EmpJobPostAdapter.EmpJobPostViewHolder>() {
    private var jobpostList= arrayListOf<PostDataModel>()
    class EmpJobPostViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpJobPostViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: EmpJobPostViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}