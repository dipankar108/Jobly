package com.university_project.jobly.accountlog

import android.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.university_project.jobly.adapter.SkillAdapter
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.baseviewmodel.profile.UserViewModel
import com.university_project.jobly.databinding.ActivityUpdateProfileBinding
import com.university_project.jobly.employee.EmployeeActivity
import com.university_project.jobly.interfaces.SkillClick
import com.university_project.jobly.utils.SharedInfo

class UpdateProfileActivity : AppCompatActivity(), SkillClick {
    private lateinit var liveData: BaseViewModel
    private lateinit var updateProfileLiveData: UserViewModel
    private lateinit var binding: ActivityUpdateProfileBinding
    private var selectedSkills = mutableListOf<String>()
    private var skills = listOf<String>()
    private var userPass = ""
    private var cvEmp = ""
    private var verified = false
    private var pdfUri = Uri.EMPTY
    private lateinit var skillTextAdapter: ArrayAdapter<String>
    private var storageRef = Firebase.storage
    private lateinit var dialog: AlertDialog.Builder
    private val skillAdapter = SkillAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        val sh = getSharedPreferences(SharedInfo.USER.user, MODE_PRIVATE)
        binding.etUpSkillId.hint = "Add Skill"
        binding.rvUpSkillId.layoutManager = LinearLayoutManager(this).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.rvUpSkillId.adapter = skillAdapter
        val userInfo = sh.getString(SharedInfo.USER_TYPE.user, null)
        updateProfileLiveData = ViewModelProvider(this)[UserViewModel::class.java]
        dialog = AlertDialog.Builder(this)

        if (userInfo == "Client") {

        } else {
            liveData.getEmployeeProfile().observe(this) { user ->
                if (user.verify) {
                    binding.tvUpVerifyInfoId.text = "You are Verified"
                } else {
                    binding.tvUpVerifyInfoId.text = "You are Unverified"
                }
                binding.etUpFnameId.setText(user.fname)
                binding.etUpLnameId.setText(user.lname)
                binding.etUpAboutYourselfId.setText(user.aboutYourself)
                binding.etUpEmailId.setText(user.userEmail)
                binding.etUpEmailId.isEnabled = false
                if (user.cvEmp != "No CV") {
                    binding.btnUploadCVId.text = "Change CV"
                }
                verified = user.verify
                binding.etUpYourHobbyId.setText(user.hobbyEmp)
                selectedSkills = user.skill
                skillAdapter.setList(selectedSkills)
                skillAdapter.notifyDataSetChanged()
                userPass = user.userPass
                cvEmp = user.cvEmp
            }
            liveData.getSkill().observe(this) { skill ->
                skills = skill
                skillTextAdapter =
                    ArrayAdapter(this, R.layout.simple_dropdown_item_1line, skills)
                binding.etUpSkillId.setAdapter(skillTextAdapter)
            }
            binding.etUpSkillId.setOnItemClickListener { adapterView, view, i, l ->

                val skillName = skillTextAdapter.getItem(i).toString().lowercase()
                if (selectedSkills.contains(skillName)) {
                    showToast("Already Added", this)
                } else selectedSkills.add(skillName)
                binding.etUpSkillId.text.clear()
                if (selectedSkills.size == 5) {
                    binding.etUpSkillId.isEnabled = false
                    binding.etUpSkillId.hint = "Remove one skill to enable"
                    showToast("Max 5 allowed", this)
                }
                skillAdapter.notifyDataSetChanged()
            }
        }

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            pdfUri = uri
        }
        binding.btnUploadCVId.setOnClickListener {
            getContent.launch("application/pdf")
        }
        binding.etUpFnameId.setOnClickListener {
            updateProfileWithDialog(binding.etUpFnameId.text.toString(), "First Name", "fname")
        }
        binding.etUpLnameId.setOnClickListener {
            updateProfileWithDialog(binding.etUpLnameId.text.toString(), "Last Name", "lname")

        }
        binding.etUpEmailId.isEnabled = false
        binding.etUpAboutYourselfId.setOnClickListener {
            updateProfileWithDialog(
                binding.etUpAboutYourselfId.text.toString(),
                "About YourSelf",
                "yourself"
            )
        }
        binding.etUpYourHobbyId.setOnClickListener {
            updateProfileWithDialog(
                binding.etUpYourHobbyId.text.toString(),
                "Your Hobby",
                "hobby"
            )
        }
        binding.btnUpSubmitId.setOnClickListener {
            val mstorageRef =
                storageRef.reference.child("cvPDF/${System.currentTimeMillis()}${Firebase.auth.uid}")
            mstorageRef.putFile(pdfUri)
                .addOnSuccessListener {
                    mstorageRef.downloadUrl.addOnSuccessListener { cvUrl ->
                        if (cvUrl != null) {

                        }
                    }
                }
        }
    }

    private fun updateProfileWithDialog(plaintext: String, hintText: String, field: String) {
        val view = layoutInflater.inflate(
            com.university_project.jobly.R.layout.updateprofiledialog,
            null,
            false
        )
        val inputText =
            view.findViewById<EditText>(com.university_project.jobly.R.id.et_bottomFragment_Update_id)
        val titleText =
            view.findViewById<TextView>(com.university_project.jobly.R.id.tv_titleview_update_id)
        val btnSubmit =
            view.findViewById<Button>(com.university_project.jobly.R.id.btn_submit_update_id)
        dialog.setView(view).show()
        inputText.setText(plaintext)
        titleText.text = hintText
        btnSubmit.setOnClickListener {
            updateProfileLiveData.updateProfile(inputText.text.toString(), field)
        }
    }


    private fun showToast(s: String, context: Context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun onSkillDeleteClick(skill: String) {
        selectedSkills.remove(skill)
        skillAdapter.notifyDataSetChanged()
        binding.etUpSkillId.isEnabled = true
        binding.etUpSkillId.hint = "Add Skill"
    }

    override fun onBackPressed() {
        backToHome()
    }

    private fun backToHome() {
        val intent = Intent(this, EmployeeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}

