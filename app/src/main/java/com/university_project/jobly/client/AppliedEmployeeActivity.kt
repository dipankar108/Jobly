package com.university_project.jobly.client

import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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

class AppliedEmployeeActivity : AppCompatActivity(), SPAppliedEmpClick {
    private val TAG = "AppliedEmployeeActivityP"
    private lateinit var binding: ActivityAppliedEmployeeBinding
    private lateinit var liveData: ClientPostViewModel
    private val mcontext = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppliedEmployeeBinding.inflate(layoutInflater)
        val actionbar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#79AA8D"))
        actionbar?.setBackgroundDrawable(colorDrawable)
        setContentView(binding.root)
        val myAdapter = SPAppliedEmpAdapter(this)
        val docId = intent.getStringExtra("docId")
        binding.rvAppliedEmployeeId.layoutManager = LinearLayoutManager(this)
        binding.rvAppliedEmployeeId.adapter = myAdapter
        liveData = ViewModelProvider(this)[ClientPostViewModel::class.java]
        liveData.getAppliedEmployee(docId!!).observe(this) {
            myAdapter.setData(it)
            myAdapter.notifyDataSetChanged()
        }
    }

    override fun onAcceptEmp(appliedDataModel: AppliedDataModel) {
        Repository.updateCallforInterView(appliedDataModel.docId, appliedDataModel.employeeId)
        Repository.updateAppliedEmployye(appliedDataModel)
        Repository.createChatDoc(appliedDataModel)
    }

    override fun onDownloadEmp(downloadLink: String) {
        Log.d(TAG, "onDownloadEmp: Donload $downloadLink")
        val req = DownloadManager.Request(Uri.parse(downloadLink))
            .setDescription(Environment.DIRECTORY_DOWNLOADS)
            .setTitle("CV${System.currentTimeMillis()}")
            .setDescription("Downloading")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(req)
    }
}