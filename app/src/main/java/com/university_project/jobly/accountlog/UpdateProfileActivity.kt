package com.university_project.jobly.accountlog

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.university_project.jobly.R
import com.university_project.jobly.adapter.SkillAdapter
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.databinding.ActivityUpdateProfileBinding
import com.university_project.jobly.interfaces.SkillClick

class UpdateProfileActivity : AppCompatActivity(), SkillClick {
    private lateinit var liveData: BaseViewModel
    private lateinit var binding: ActivityUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        val sh = getSharedPreferences("userType", MODE_PRIVATE)
        val skillAdapter = SkillAdapter(this)
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
                binding.etUpEmailId.setText(user.userPass)
                binding.etUpEmailId.isEnabled = false
                binding.etUpYourHobbyId.setText(user.hobbyEmp)
                Log.d("TAG", "onCreate: ${user.skill}")
                skillAdapter.setList(user.skill)
                skillAdapter.notifyDataSetChanged()
            })
        }
    }

    override fun onSkillDeleteClick(skill: String) {
        Log.d("TAG", "onSkillDeleteClick: $skill")
    }
}