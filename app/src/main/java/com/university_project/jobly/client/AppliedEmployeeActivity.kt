package com.university_project.jobly.client

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.university_project.jobly.client.adapter.AppliedEmployeeAdapter
import com.university_project.jobly.client.clientviewmodel.ClientPostViewModel
import com.university_project.jobly.databinding.ActivityAppliedEmployeeBinding

class AppliedEmployeeActivity : AppCompatActivity() {
    private val TAG = "AppliedEmployeeActivityP"
    private lateinit var binding: ActivityAppliedEmployeeBinding
    private lateinit var liveData: ClientPostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppliedEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myAdapter = AppliedEmployeeAdapter()
        val docId = intent.getStringExtra("docId")
        Log.d(TAG, "onCreate: $docId")
        binding.rvAppliedEmployeeId.layoutManager = LinearLayoutManager(this)
        binding.rvAppliedEmployeeId.adapter = myAdapter
        liveData = ViewModelProvider(this)[ClientPostViewModel::class.java]
        liveData.getAppliedEmployee(docId!!).observe(this, {
            Log.d(TAG, "onCreate: $it")
            myAdapter.setData(it)
            myAdapter.notifyDataSetChanged()
        })
        /**      liveData.postList.observe(this, { list ->
        var myList = listOf<AppliedEmployeeDataModel>()
        list.forEach {
        myList += listOf(
        AppliedEmployeeDataModel(
        it.userId,
        it.appliedEmployee,
        it.docId,
        it.call_for_interview
        )
        )
        }
        Log.d(TAG, "onCreate: $myList")
        myAdapter.setData(myList)
        myAdapter.notifyDataSetChanged()

        })
         **/
    }
}