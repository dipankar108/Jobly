package com.university_project.jobly

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.adapter.SkillAdapter
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.client.ClientActivity
import com.university_project.jobly.databinding.ActivityCreateJobPostBinding
import com.university_project.jobly.datamodel.AppliedDataModel
import com.university_project.jobly.datamodel.CallForInterViewDataModel
import com.university_project.jobly.interfaces.SkillClick

class CreateJobPost : AppCompatActivity(), SkillClick {
    lateinit var auth: FirebaseAuth
    val TAG = "TAG"
    private val genderList = listOf("Any", "Male", "Female")
    private lateinit var liveData: BaseViewModel
    private var skills = listOf<String>()
    private var selectedSkills = mutableListOf<String>()
    private lateinit var skillAdapter: ArrayAdapter<String>
    private lateinit var binding: ActivityCreateJobPostBinding
    val rvskillAdapter = SkillAdapter(this)
    private lateinit var dialog: Dialog
    private var attachmentLink = Uri.EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateJobPostBinding.inflate(layoutInflater)
        val actionbar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#79AA8D"))
        actionbar?.setBackgroundDrawable(colorDrawable)
        setContentView(binding.root)
        auth = Firebase.auth
        dialog = Dialog(this)
        binding.rvSkillViewId.layoutManager = LinearLayoutManager(this).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.rvSkillViewId.adapter = rvskillAdapter
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        liveData.getSkill().observe(this) { skill ->
            skills = skill
            Log.d(TAG, "onCreate: $skill")
            skillAdapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, skills)
            binding.amEtSkillCreatePostId.setAdapter(skillAdapter)
        }
        binding.amEtSkillCreatePostId.setOnItemClickListener { _, _, i, _ ->
            val skillName = skillAdapter.getItem(i).toString().lowercase()
            if (selectedSkills.contains(skillName)) {
                showToast("Already Added", this)
            } else selectedSkills.add(skillName)
            binding.amEtSkillCreatePostId.text.clear()
            if (selectedSkills.size == 5) {
                binding.amEtSkillCreatePostId.isEnabled = false
                binding.amEtSkillCreatePostId.hint = "Remove one skill to enable"
                showToast("Max 5 allowed", this)
            }
            rvskillAdapter.setList(selectedSkills)
            rvskillAdapter.notifyDataSetChanged()
        }
        val spinnerGenderAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, genderList
        )
        val uploadPdf =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { pdfFile: ActivityResult ->
                if (pdfFile.resultCode == Activity.RESULT_OK) {
                    attachmentLink = Uri.parse(pdfFile.data?.data.toString())
                    binding.btnUploadAttachmentId.text = "CHANGE ATTACHMENT"
                }
            }
        binding.btnUploadAttachmentId.setOnClickListener {
            val intent = Intent()
            intent.type = ("application/pdf")
            intent.action = Intent.ACTION_GET_CONTENT
            uploadPdf.launch(intent)
        }
        binding.spGenderCreatePostId.adapter = spinnerGenderAdapter
        binding.btnCreatePostId.setOnClickListener {
            dialog.setContentView(R.layout.progressbarlayout)
            dialog.setCancelable(false)
            try {
                val postTitle = binding.etTitleCreatePostId.text.toString()
                val postDesc = binding.etDescCratePostId.text.toString()
                val postExperience = Integer.parseInt(binding.etRegExperienceId.text.toString())
                val postSkills = selectedSkills
                val postSalary = binding.etRegSalaryId.text.toString().toInt()
                val postGender = binding.spGenderCreatePostId.selectedItem.toString()
                val timeStamp = System.currentTimeMillis()
                val callforinterview: ArrayList<CallForInterViewDataModel> = ArrayList()
                val isLike = ArrayList<String>()
                val appliedEmployee = ArrayList<AppliedDataModel>()
                if (alltextField(
                        postTitle,
                        postDesc,
                        postExperience,
                        postSkills,
                        postSalary,
                        postGender
                    )
                ) {
                    dialog.show()
                    liveData.makePost(
                        postTitle,
                        postDesc,
                        postExperience,
                        postSkills,
                        postSalary,
                        postGender,
                        isLike,
                        callforinterview,
                        timeStamp,
                        appliedEmployee,
                        attachmentLink
                    ).observe(this) {

                        dialog.dismiss()
                        if (it) {
                            dialog.dismiss()
                            val intent = Intent(this, ClientActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Please complete your profile", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(this, "No field can be empty", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "No field can be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun alltextField(
        postTitle: String,
        postDesc: String,
        postExperience: Int,
        postSkills: MutableList<String>,
        postSalary: Int,
        postGender: String
    ): Boolean {
        return when {
            postTitle.isEmpty() -> {
                false
            }
            postDesc.isEmpty() -> {
                false
            }
            postExperience < 0 -> {
                false
            }
            postSkills.isEmpty() -> {
                false
            }
            postSalary < 0 -> {
                false
            }
            postGender.isEmpty() -> {
                false
            }
            else -> {
                true
            }
        }
    }

    private fun showToast(s: String, context: Context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun onSkillDeleteClick(skill: String) {
        selectedSkills.remove(skill)
        rvskillAdapter.notifyDataSetChanged()
        binding.amEtSkillCreatePostId.isEnabled = true
        binding.amEtSkillCreatePostId.hint = "Add Skill"
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Destroyed")
    }
}