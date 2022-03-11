package com.university_project.jobly.accountlog

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.adapter.SkillAdapter
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.databinding.ActivityUpdateProfileBinding
import com.university_project.jobly.datamodel.EmployeeProfileModel
import com.university_project.jobly.employee.EmployeeActivity
import com.university_project.jobly.interfaces.SkillClick

class UpdateProfileActivity : AppCompatActivity(), SkillClick {
    private lateinit var liveData: BaseViewModel
    private lateinit var binding: ActivityUpdateProfileBinding
    private var selectedSkills = mutableListOf<String>()
    private var skills = listOf<String>()
    private var userPass = ""
    private var cvEmp=""
    private var verified = false
    private lateinit var skillTextAdapter: ArrayAdapter<String>
    val skillAdapter = SkillAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        val sh = getSharedPreferences("userType", MODE_PRIVATE)
        binding.etUpSkillId.hint = "Add Skill"
        binding.rvUpSkillId.layoutManager = LinearLayoutManager(this).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.rvUpSkillId.adapter = skillAdapter
        val userInfo = sh.getString("m_userType", null)
        if (userInfo == "Client") {
        } else {
            liveData.getEmployeeProfile().observe(this, { user ->
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
                verified = user.verify
                binding.etUpYourHobbyId.setText(user.hobbyEmp)
                selectedSkills = user.skill
                skillAdapter.setList(selectedSkills)
                skillAdapter.notifyDataSetChanged()
                userPass = user.userPass
                cvEmp=user.cvEmp
            })
            liveData.getSkill().observe(this, { skill ->
                skills = skill
                skillTextAdapter =
                    ArrayAdapter(this, R.layout.simple_dropdown_item_1line, skills)
                binding.etUpSkillId.setAdapter(skillTextAdapter)
            })
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
        binding.btnUpSubmitId.setOnClickListener {
            val fname = binding.etUpFnameId.text.toString()
            val lname = binding.etUpLnameId.text.toString()
            val userId = Firebase!!.auth.uid.toString()
            val userEmail = binding.etUpEmailId.text.toString()
            val userType = "Employee"
            val skill = selectedSkills
            val currentCompany = ""
            val hobby = binding.etUpYourHobbyId.text.toString()
            val yourself = binding.etUpAboutYourselfId.text.toString()
            val banned = false
            val verify = verified
            val employeeProfileModel = EmployeeProfileModel(
                userId, fname, lname, userEmail, userPass, userType,
                skill as ArrayList<String>, currentCompany, hobby, yourself, verify, banned,cvEmp
            )
            liveData.updateEmpProfile(employeeProfileModel)
            backToHome()
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

    fun backToHome() {
        val intent = Intent(this, EmployeeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}