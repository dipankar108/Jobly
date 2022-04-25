package com.university_project.jobly

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.baseviewmodel.profile.UserViewModel
import com.university_project.jobly.databinding.ActivityUpdateClientProfileBinding
import com.university_project.jobly.utils.SharedInfo

class UpdateClientProfile : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateClientProfileBinding
    private lateinit var liveData: BaseViewModel
    private lateinit var updateProfileLiveData: UserViewModel
    private var verified = false
    private lateinit var dialog: Dialog
    private var companys = listOf<String>()
    private var selectedCompany = ""
    private lateinit var companyTextAdapter: ArrayAdapter<String>
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
                    binding.tvUpeVerifyInfoId.text = "You are Verified"
                } else {
                    binding.tvUpeVerifyInfoId.text = "You are Unverified"
                }
                liveData.getCompany().observe(this) { comp ->
                    companys = comp
                    companyTextAdapter =
                        ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, companys)
                    binding.etUpeCnameId.setAdapter(companyTextAdapter)
                }
                binding.etUpeCnameId.setOnItemClickListener { _, _, i, _ ->
                    val skillName = companyTextAdapter.getItem(i).toString()
                    updateProfileLiveData.updateProfile(skillName, "companyName")
                    binding.etUpeCnameId.text.clear()
                    binding.etUpeCnameId.hint = "Edit Company"
                    val inputMethodManager =
                        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.etUpeCnameId.windowToken, 0)
                }
                Glide.with(this)
                    .load(user.profileImg)
                    .placeholder(R.drawable.image_loding_anim)
                    .error(R.drawable.try_later)
                    .into(binding.ivUpeProfilePicId)
                binding.etUpeCNameViewId.text = user.companyName
                binding.etUpeFnameId.text = user.fname
                binding.etUpeLnameId.text = user.lname
                binding.etUpeCLocationId.text = user.companyLocation
                binding.etUpeAboutYourselfId.text = user.yourself
                binding.etUpeEmailId.setText(user.userEmail)
                verified = user.verify
                binding.etUpeYourHobbyId.text = user.hobby
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

}
