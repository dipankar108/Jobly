package com.university_project.jobly.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.university_project.jobly.R
import com.university_project.jobly.interfaces.CompanyClick

class CompanyAdapter(private val listener: CompanyClick) :
    RecyclerView.Adapter<CompanyAdapter.comAdapterViewHolder>() {
    private var skill = listOf<String>()

    class comAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val skillName: TextView = itemView.findViewById(R.id.tv_skillTitle_id)
        val delete: ImageView = itemView.findViewById(R.id.img_skillImg_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): comAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.skillitemview, parent, false)
        return comAdapterViewHolder(view)
    }


    override fun getItemCount(): Int {
        return skill.size
    }


    fun setList(skill: List<String>) {
        this.skill = skill
    }

    override fun onBindViewHolder(holder: comAdapterViewHolder, position: Int) {
        holder.skillName.text = skill[position]
        holder.delete.setOnClickListener {
            listener.onCompDeleteClick(skill[position])
        }
    }

}