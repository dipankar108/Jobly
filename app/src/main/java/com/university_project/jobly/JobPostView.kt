package com.university_project.jobly

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.client.ClientActivity
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.databinding.ActivityJobPostViewBinding
import com.university_project.jobly.employee.EmployeeActivity

class JobPostView : AppCompatActivity() {
    private lateinit var binding: ActivityJobPostViewBinding
    lateinit var docID: String
    val TAG = "TAG"
    private lateinit var auth: FirebaseAuth
    private lateinit var singlePostView: PostDataModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobPostViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras
        docID = bundle!!.getString("docId").toString()
        auth = Firebase.auth!!
        val db = Firebase.firestore.collection("JobPost").document(docID).get()
        db.addOnSuccessListener {
            singlePostView = it.toObject(PostDataModel::class.java)!!
            binding.tvSinglePostViewTitleId.text = singlePostView.title
            binding.tvSinglePostViewDescId.text = singlePostView.desc
            binding.tvSinglePostViewCompanyNameId.text = singlePostView.companyName
            binding.tvSinglePostViewSalaryId.text = singlePostView.Salary.toString()
            binding.tvSinglePostViewExperienceId.text = singlePostView.experience.toString()
            binding.tvSinglePostViewJobPositionId.text = singlePostView.category[0]
            binding.tvSinglePostViewLocationId.text = singlePostView.location
        }.addOnFailureListener { e ->
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        val sh = getSharedPreferences("userType", MODE_PRIVATE)
        val userInfo = sh.getString("m_userType", null)
        setBtnText(userInfo!!)
        binding.btnSinglePostViewSubmitId.setOnClickListener {
            if (userInfo == "Client") {
                Log.d(TAG, "onCreate: Go to edit page")
            } else {
                Log.d(TAG, "onCreate: Go to apply page")
            }
        }
    }

    private fun setBtnText(string: String?) {
        if (string == "Client") {
            binding.btnSinglePostViewSubmitId.text = "Edit"
        } else binding.btnSinglePostViewSubmitId.text = "Apply"
    }

    override fun onBackPressed() {
        super.onBackPressed()
       if (getSharedPreferences("userType", MODE_PRIVATE).getString("m_userType",null)=="Client"){
           changeActivity(ClientActivity())
       }else changeActivity(EmployeeActivity())
    }

    private fun changeActivity(activity:Activity) {
        val intent = Intent(applicationContext, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}