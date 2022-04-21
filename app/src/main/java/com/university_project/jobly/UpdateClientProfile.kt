package com.university_project.jobly

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.baseviewmodel.profile.UserViewModel
import com.university_project.jobly.databinding.ActivityUpdateClientProfileBinding
import com.university_project.jobly.employee.EmployeeActivity
import com.university_project.jobly.utils.SharedInfo

class UpdateClientProfile : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateClientProfileBinding
    private lateinit var liveData: BaseViewModel
    private lateinit var updateProfileLiveData: UserViewModel
    private var verified = false
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateClientProfileBinding.inflate(layoutInflater)
        val actionbar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#79AA8D"))
        actionbar?.setBackgroundDrawable(colorDrawable)
        setContentView(binding.root)
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        val sh = getSharedPreferences(SharedInfo.USER.user, MODE_PRIVATE)
        val userInfo = sh.getString(SharedInfo.USER_TYPE.user, null)
        updateProfileLiveData = ViewModelProvider(this)[UserViewModel::class.java]
        dialog = Dialog(this)
        if (userInfo == "Client") {
            liveData.getClientProfile().observe(this) { user ->
                if (user.verify) {
                    binding.tvUpVerifyInfoId.text = "You are Verified"
                } else {
                    binding.tvUpVerifyInfoId.text = "You are Unverified"
                }
                binding.etUpeFnameId.text = user.fname
                binding.etUpeLnameId.text = user.lname
                binding.etUpeCLocationId.text = user.companyLocation
                binding.etUpeCnameId.text = user.companyName
                binding.etUpeAboutYourselfId.text = user.aboutYourself
                binding.etUpeEmailId.setText(user.userEmail)
                verified = user.verify
                binding.etUpeYourHobbyId.setText(user.hobbyClient)
            }
        }

        binding.etUpeFnameId.setOnClickListener {
            updateProfileWithDialog(binding.etUpeFnameId.text.toString(), "First Name", "fname")
        }
        binding.etUpeLnameId.setOnClickListener {
            updateProfileWithDialog(binding.etUpeLnameId.text.toString(), "Last Name", "lname")
        }
        binding.etUpeCLocationId.setOnClickListener {
            updateProfileWithDialog(
                binding.etUpeCLocationId.text.toString(),
                "Last Name",
                "companyLocation"
            )
        }
        binding.etUpeCnameId.setOnClickListener {
            updateProfileWithDialog(
                binding.etUpeCnameId.text.toString(),
                "Company Name",
                "companyName"
            )
        }
        binding.etUpeAboutYourselfId.setOnClickListener {
            updateProfileWithDialog(
                binding.etUpeAboutYourselfId.text.toString(),
                "First Name",
                "aboutYourself"
            )
        }
        binding.etUpeYourHobbyId.setOnClickListener {
            updateProfileWithDialog(
                binding.etUpeYourHobbyId.text.toString(),
                "First Name",
                "hobbyClient"
            )
        }
        binding.etUpeEmailId.isEnabled = false
        binding.etUpeAboutYourselfId.setOnClickListener {
            updateProfileWithDialog(
                binding.etUpeAboutYourselfId.text.toString(),
                "About YourSelf",
                "yourself"
            )
        }
        binding.etUpeYourHobbyId.setOnClickListener {
            updateProfileWithDialog(
                binding.etUpeYourHobbyId.text.toString(),
                "Your Hobby",
                "hobby"
            )
        }
    }

    private fun updateProfileWithDialog(plaintext: String, hintText: String, field: String) {
        val view = layoutInflater.inflate(
            R.layout.updateprofiledialog,
            null,
            false
        )
        val inputText =
            view.findViewById<EditText>(R.id.et_bottomFragment_Update_id)
        val titleText =
            view.findViewById<TextView>(R.id.tv_titleview_update_id)
        val btnSubmit =
            view.findViewById<Button>(R.id.btn_submit_update_id)
        dialog.setContentView(view)
        dialog.show()
        inputText.setText(plaintext)
        titleText.text = hintText
        btnSubmit.setOnClickListener {
            updateProfileLiveData.updateProfile(inputText.text.toString(), field)
            dialog.dismiss()
        }
    }


    private fun showToast(s: String, context: Context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
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

