package com.university_project.jobly.accountlog

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.university_project.jobly.R
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.databinding.ActivityUpdateProfileBinding

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var liveData: BaseViewModel
    private lateinit var isVerifiedView: TextView
    private lateinit var fname: EditText
    private lateinit var lname: EditText
    private lateinit var yourself: EditText
    private lateinit var email: EditText
    private lateinit var hobby: EditText
    private lateinit var binding: ActivityUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        val sh = getSharedPreferences("userType", MODE_PRIVATE)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        isVerifiedView = findViewById(R.id.tv_up_verifyInfo_id)
        fname = findViewById(R.id.et_up_fname_id)
        val userInfo = sh.getString("m_userType", null)
        if (userInfo == "Client") {

        } else {
            liveData.getEmployeeProfile().observe(this, { user ->
                if (user.verify) {
                    isVerifiedView.text = "You are Verified"
                } else {
                    isVerifiedView.text = "You are Unverified"
                }
                fname.setText(user.fname)
                lname.setText(user.lname)
                yourself.setText(user.aboutYourself)
                email.setText(user.userPass)
                hobby.setText(user.hobbyEmp)
            })
        }
    }
}