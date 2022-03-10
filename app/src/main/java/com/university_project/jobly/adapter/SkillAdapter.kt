package com.university_project.jobly.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.university_project.jobly.R

class SkillAdapter() : RecyclerView.Adapter<SkillAdapter.skillAdapterViewHolder>() {
    private var skill = listOf<String>()

    class skillAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val skillName: TextView = itemView.findViewById(R.id.tv_skillTitle_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): skillAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.skillitemview, parent, false)
        return skillAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: skillAdapterViewHolder, position: Int) {
        holder.skillName.text = skill[position]
    }

    override fun getItemCount(): Int {
        return skill.size
    }

    fun setList(skill: List<String>) {
        this.skill = skill
    }
}