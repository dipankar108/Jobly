package com.university_project.jobly

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.university_project.jobly.accountlog.UpdateProfileActivity
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.client.ClientActivity
import com.university_project.jobly.client.clientviewmodel.ClientPostViewModel
import com.university_project.jobly.databinding.ActivityJobPostViewBinding
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.employee.EmployeeActivity
import com.university_project.jobly.utils.DownloadFile
import com.university_project.jobly.utils.SharedInfo

class JobPostView : AppCompatActivity() {
    private lateinit var binding: ActivityJobPostViewBinding
    lateinit var docID: String
    val TAG = "TAG"
    private var attachmentLink = Uri.EMPTY

    private lateinit var dialog: Dialog
    private lateinit var auth: FirebaseAuth
    private lateinit var singlePostView: PostDataModel
    private lateinit var liveData: ClientPostViewModel
    private lateinit var pleasedialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobPostViewBinding.inflate(layoutInflater)
        val actionbar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#79AA8D"))
        actionbar?.setBackgroundDrawable(colorDrawable)
        setContentView(binding.root)
        val bundle = intent.extras
        dialog = Dialog(this)
        pleasedialog = Dialog(this)
        pleasedialog.setContentView(R.layout.progressbarlayout)
        docID = bundle!!.getString("docId").toString()
        auth = Firebase.auth!!
        liveData = ViewModelProvider(this)[ClientPostViewModel::class.java]
        liveData.getSinglePost(docID).observe(this) { singlePostView ->
            Log.d(TAG, "onCreate: ${singlePostView.salary}")
            this.singlePostView = singlePostView
            val nList = singlePostView.appliedEmployee.toList()
            if (nList.any { it.employeeId == auth.uid.toString() }) {
                binding.btnSinglePostViewSubmitId.isEnabled = false
            }
            if (singlePostView.attachment == "No Attachment") {
                binding.btnSinglePostViewDownId.isEnabled = false
            }
            binding.tvSinglePostViewTitleId.text = singlePostView.title
            binding.tvSinglePostViewDescId.text = singlePostView.desc
            binding.tvSinglePostViewCompanyNameId.text = singlePostView.companyName
            binding.tvSinglePostViewSalaryId.text = singlePostView.salary.toString()
            binding.tvSinglePostViewExperienceId.text = singlePostView.experience.toString()
            //binding.tvSinglePostViewJobPositionId.text = singlePostView.skill[0]
            binding.tvSinglePostViewLocationId.text = singlePostView.location
        }
        val sh = getSharedPreferences(SharedInfo.USER.user, MODE_PRIVATE)
        val userInfo = sh.getString(SharedInfo.USER_TYPE.user, null)
        setBtnText(userInfo)
        if (userInfo == "Client") {
            val view = layoutInflater.inflate(R.layout.updateprofiledialog, null, false)
            val btnInput: Button = view.findViewById(R.id.btn_submit_update_id)
            val textInput: EditText = view.findViewById(R.id.et_bottomFragment_Update_id)
            val titleView: TextView = view.findViewById(R.id.tv_titleview_update_id)
            binding.tvSinglePostViewTitleId.setOnClickListener {
                dialog.setContentView(view)
                titleView.text = "Change Title"
                textInput.setText(singlePostView.title)
                btnInput.setOnClickListener {
                    Repository.updatePost("title", textInput.text.toString(), docID)
                    dialog.dismiss()
                }
                dialog.show()
            }
            binding.tvSinglePostViewDescId.setOnClickListener {
                dialog.setContentView(view)
                titleView.text = "Change Description"
                textInput.maxLines = 3
                textInput.setText(singlePostView.desc)
                btnInput.setOnClickListener {
                    Repository.updatePost("desc", textInput.text.toString(), docID)
                    dialog.dismiss()
                }
                dialog.show()
            }
            binding.tvSinglePostViewExperienceId.setOnClickListener {
                dialog.setContentView(view)
                titleView.text = "Change Experience"
                textInput.maxLines = 1
                textInput.inputType = InputType.TYPE_CLASS_NUMBER
                textInput.setText(singlePostView.experience.toString())
                btnInput.setOnClickListener {
                    textInput.text.toString()?.let {
                        Repository.updatePost("experience", it, docID)
                    }
                    dialog.dismiss()
                }


                dialog.show()
            }
            binding.tvSinglePostViewSalaryId.setOnClickListener {
                dialog.setContentView(view)
                titleView.text = "Change Salary Amount"
                textInput.maxLines = 1
                textInput.inputType = InputType.TYPE_CLASS_NUMBER
                textInput.setText(singlePostView.salary.toString())
                btnInput.setOnClickListener {
                    textInput.text.toString()?.let {
                        Repository.updatePost("salary", it, docID)
                    }
                    dialog.dismiss()
                }
                dialog.show()
            }
            binding.tvSinglePostViewLocationId.setOnClickListener {
                dialog.setContentView(view)
                titleView.text = "Change Location"
                textInput.maxLines = 2
                textInput.setText(singlePostView.location)
                btnInput.setOnClickListener {
                    Repository.updatePost("location", textInput.text.toString(), docID)
                    dialog.dismiss()
                }
                dialog.show()
            }
            binding.tvSinglePostViewCompanyNameId.setOnClickListener {
                dialog.setContentView(view)
                titleView.text = "Change CompanyName"
                textInput.maxLines = 1
                textInput.setText(singlePostView.companyName)
                btnInput.setOnClickListener {
                    Repository.updatePost("companyName", textInput.text.toString(), docID)
                    dialog.dismiss()
                }

                dialog.show()
            }
            binding.btnSinglePostViewSubmitId.text = "You can Edit Post"
            binding.btnSinglePostViewSubmitId.isEnabled = false
        }
        val uploadPdf =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { pdfFile: ActivityResult ->
                if (pdfFile.resultCode == Activity.RESULT_OK) {
                    pleasedialog.show()
                    pleasedialog.setCancelable(false)
                    attachmentLink = Uri.parse(pdfFile.data?.data.toString())
                    binding.btnSinglePostViewDownId.text = "Attachment Changed"
                    val mstorageRef =
                        Firebase.storage.reference.child("attachPDF/${System.currentTimeMillis()}${Firebase.auth.uid}")
                    mstorageRef.putFile(attachmentLink)
                        .addOnSuccessListener {
                            mstorageRef.downloadUrl.addOnSuccessListener {
                                Repository.updatePost("attachment", it.toString(), docID)
                                pleasedialog.dismiss()
                            }
                        }
                }
            }
        binding.btnSinglePostViewDownId.setOnClickListener {
            if (userInfo == "Client") {
                val intent = Intent()
                intent.type = ("application/pdf")
                intent.action = Intent.ACTION_GET_CONTENT
                uploadPdf.launch(intent)
            } else {
                DownloadFile.downloadFile(singlePostView.attachment, singlePostView.title, this)
                /**            val req = DownloadManager.Request(Uri.parse(singlePostView.attachment))
                .setDescription(Environment.DIRECTORY_DOWNLOADS)
                .setTitle("${singlePostView.title}")
                .setDescription("Downloading")
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(req)*/
            }
        }
//        setBtnText(userInfo!!)
        binding.btnSinglePostViewSubmitId.setOnClickListener {
            if (userInfo == "Client") {

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
                latestExampleEmailCreation(
                    arrayOf("dipankar0debnath@gmail.com"),
                    "Report Post",
                    "Please dont edit this id: $docID"
                )
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
            binding.btnSinglePostViewSubmitId.visibility = View.VISIBLE
            binding.btnSinglePostViewDownId.text = "Change Attachment"
        } else binding.btnSinglePostViewSubmitId.visibility = View.VISIBLE
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
        addresses: Array<String>, subject: String, text: String
    ) {
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