package com.university_project.jobly.client

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.client.adapter.SPAppliedEmpAdapter
import com.university_project.jobly.client.clientviewmodel.ClientPostViewModel
import com.university_project.jobly.client.interfaces.SPAppliedEmpClick
import com.university_project.jobly.databinding.ActivityAppliedEmployeeBinding
import com.university_project.jobly.datamodel.AppliedDataModel

class AppliedEmployeeActivity : AppCompatActivity(),SPAppliedEmpClick {
    private val TAG = "AppliedEmployeeActivityP"
    private lateinit var binding: ActivityAppliedEmployeeBinding
    private lateinit var liveData: ClientPostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppliedEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myAdapter = SPAppliedEmpAdapter(this)
        val docId = intent.getStringExtra("docId")
        binding.rvAppliedEmployeeId.layoutManager = LinearLayoutManager(this)
        binding.rvAppliedEmployeeId.adapter = myAdapter
        liveData = ViewModelProvider(this)[ClientPostViewModel::class.java]
        liveData.getAppliedEmployee(docId!!).observe(this, {
            myAdapter.setData(it)
            myAdapter.notifyDataSetChanged()
        })
    }

    override fun onAcceptEmp(appliedDataModel: AppliedDataModel) {
        Log.d(TAG, "onAcceptEmp: $appliedDataModel")
        Repository.updateCallforInterView(appliedDataModel.docId,appliedDataModel.employeeId)
        Repository.createChatDoc(appliedDataModel)
    }
}