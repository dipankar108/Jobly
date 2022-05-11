package com.university_project.jobly

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.accountlog.UpdateProfileActivity
import com.university_project.jobly.client.ClientActivity
import com.university_project.jobly.client.clientviewmodel.ClientPostViewModel
import com.university_project.jobly.databinding.ActivityJobPostViewBinding
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.employee.EmployeeActivity
import com.university_project.jobly.utils.SharedInfo

class JobPostView : AppCompatActivity() {
    private lateinit var binding: ActivityJobPostViewBinding
    lateinit var docID: String
    val TAG = "TAG"
    private lateinit var dialog: Dialog
    private lateinit var auth: FirebaseAuth
    private lateinit var singlePostView: PostDataModel
    private lateinit var liveData: ClientPostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobPostViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras
        dialog = Dialog(this)
        docID = bundle!!.getString("docId").toString()
        auth = Firebase.auth!!
        Log.d(TAG, "onCreate: $docID")
        liveData = ViewModelProvider(this)[ClientPostViewModel::class.java]
        liveData.getSinglePost(docID).observe(this) { singlePostView ->
            binding.tvSinglePostViewTitleId.text = singlePostView.title
            binding.tvSinglePostViewDescId.text = singlePostView.desc
            binding.tvSinglePostViewCompanyNameId.text = singlePostView.companyName
            binding.tvSinglePostViewSalaryId.text = singlePostView.Salary.toString()
            binding.tvSinglePostViewExperienceId.text = singlePostView.experience.toString()
            binding.tvSinglePostViewJobPositionId.text = singlePostView.skill[0]
            binding.tvSinglePostViewLocationId.text = singlePostView.location
        }
        val sh = getSharedPreferences(SharedInfo.USER.user, MODE_PRIVATE)
        val userInfo = sh.getString(SharedInfo.USER_TYPE.user, null)
//        setBtnText(userInfo!!)
        binding.btnSinglePostViewSubmitId.setOnClickListener {
            Log.d(TAG, "onCreate: $userInfo")
            if (userInfo == "Client") {
                Log.d(TAG, "onCreate: Go to edit page")
            } else {
                liveData.appliedForPost(docID).observe(this) { applynow ->
                    when (applynow) {
                        "start" -> {
                            dialog.setContentView(R.layout.progressbarlayout)
                            dialog.show()
                        }
                        "uploaded" -> {
                            dialog.dismiss()
                        }
                        "failed" -> {
                            Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()
                        }
                        "nocv" -> {
                            dialog.dismiss()
                            val alertDialog = AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setTitle("No CV\n or Unverified")
                                .setPositiveButton(
                                    "Complete Now"
                                ) { dialogInterface, _ ->
                                    dialogInterface.dismiss()
                                    startActivity(
                                        Intent(
                                            this,
                                            UpdateProfileActivity::class.java
                                        ).apply {
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        })
                                }
                                .setNegativeButton("Later") { dialogInterface, _ ->
                                    dialogInterface.dismiss()
                                }.create()
                            alertDialog.show()
                        }
                    }
                }
//                Firebase.firestore.collection("User").document(Firebase.auth.uid.toString()).get()
//                    .addOnSuccessListener {
//                        val profile = it.toObject(EmployeeProfileModel::class.java)
//                        if (profile?.cvEmp != "No CV") {
//
//                            }
//                        } else {
//                            AlertDialog.Builder(this).setTitle("Upload CV first")
//                                .setCancelable(false)
//                                .setPositiveButton("Upload Now") { di, _ ->
//                                    di.dismiss()
//                                    startActivity(
//                                        Intent(
//                                            this,
//                                            UpdateProfileActivity::class.java
//                                        ).apply {
//                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                                        })
//                                }.setNegativeButton("No") { di, _ ->
//                                    di.dismiss()
//                                }.show()
//                        }
                // }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reportsomeone, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_report_id -> {
                latestExampleEmailCreation(arrayOf("dipankar0debnath@gmail.com"),"Report Post","Please dont edit this id: $docID")
//                startActivity(
//                    Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse("mailto:dipankar0debnath@gmail.com")
//
//                    ).apply {
//
//                        putExtra(Intent.EXTRA_SUBJECT, "Report Post")
//                        putExtra(Intent.EXTRA_TEXT, "postId : $docID")
//                    })
            }
        }
        return true
    }

    private fun setBtnText(string: String?) {
        if (string == "Client") {
            binding.btnSinglePostViewSubmitId.text = "Edit"
        } else binding.btnSinglePostViewSubmitId.text = "Apply"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (getSharedPreferences(
                SharedInfo.USER.user,
                MODE_PRIVATE
            ).getString(SharedInfo.USER_TYPE.user, null) == "Client"
        ) {
            changeActivity(ClientActivity())
        } else changeActivity(EmployeeActivity())
    }
    private fun latestExampleEmailCreation(
        addresses: Array<String>, subject: String, text: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
    private fun changeActivity(activity: Activity) {
        val intent = Intent(applicationContext, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}