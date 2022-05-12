package com.university_project.jobly.accountlog

import android.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.university_project.jobly.adapter.CompanyAdapter
import com.university_project.jobly.adapter.SkillAdapter
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.baseviewmodel.profile.UserViewModel
import com.university_project.jobly.databinding.ActivityUpdateProfileBinding
import com.university_project.jobly.employee.EmployeeActivity
import com.university_project.jobly.interfaces.CompanyClick
import com.university_project.jobly.interfaces.SkillClick
import com.university_project.jobly.utils.SharedInfo
import java.io.ByteArrayOutputStream

class UpdateProfileActivity : AppCompatActivity(), SkillClick, CompanyClick {
    private lateinit var liveData: BaseViewModel
    private lateinit var updateProfileLiveData: UserViewModel
    private lateinit var binding: ActivityUpdateProfileBinding
    private var selectedSkills = mutableListOf<String>()
    private var selectedCompany = mutableListOf<String>()
    private var skills = listOf<String>()
    private var company = listOf<String>()
    private var userPass = ""
    private var cvEmp = ""
    private var verified = false
    private var pdfUri = Uri.EMPTY
    private lateinit var skillTextAdapter: ArrayAdapter<String>
    private lateinit var comTextAdapter: ArrayAdapter<String>
    private var storageRef = Firebase.storage
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var pleasewaitdialog: Dialog
    private var imageUri = Uri.EMPTY
    private lateinit var alertDialog: android.app.AlertDialog.Builder
    private val skillAdapter = SkillAdapter(this)
    private val comAdapter = CompanyAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val actionbar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#79AA8D"))
        actionbar?.setBackgroundDrawable(colorDrawable)
        setContentView(binding.root)
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        val sh = getSharedPreferences(SharedInfo.USER.user, MODE_PRIVATE)
        binding.etUpSkillId.hint = "Add Skill"
        binding.rvUpSkillId.layoutManager = LinearLayoutManager(this).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.rvUpSkillId.adapter = skillAdapter
        binding.rvUpCompanyId.layoutManager = LinearLayoutManager(this).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.rvUpCompanyId.adapter = comAdapter
        val userInfo = sh.getString(SharedInfo.USER_TYPE.user, null)
        updateProfileLiveData = ViewModelProvider(this)[UserViewModel::class.java]
        dialog = AlertDialog.Builder(this)
        pleasewaitdialog = Dialog(this)
        alertDialog = android.app.AlertDialog.Builder(this)
        liveData.getEmployeeProfile().observe(this) { user ->
            if (user.verify) {
                binding.tvUpVerifyInfoId.text = "You are Verified"
            } else {
                binding.tvUpVerifyInfoId.text = "You are Unverified"
            }
            binding.etUpFnameId.text = user.fname
            binding.etUpLnameId.text = user.lname
            binding.etUpAboutYourselfId.text = user.aboutYourself
            binding.etUpEmailId.setText(user.userEmail)
            binding.etUpEmailId.isEnabled = false
            if (user.cvEmp != "No CV") {
                binding.btnUploadCVId.text = "Change CV"
            }
            verified = user.verify
            Glide.with(this)
                .load(user.profileImg)
                .placeholder(com.university_project.jobly.R.drawable.image_loding_anim)
                .error(com.university_project.jobly.R.drawable.try_later)
                .into(binding.ivUpProfilePicId)
            binding.etUpYourHobbyId.setText(user.hobbyEmp)
            selectedSkills = user.skill
            skillAdapter.setList(selectedSkills)
            skillAdapter.notifyDataSetChanged()
            comAdapter.setList(user.company)
            comAdapter.notifyDataSetChanged()
            userPass = user.userPass
            cvEmp = user.cvEmp
        }
        liveData.getSkill().observe(this) { skill ->
            skills = skill
            skillTextAdapter =
                ArrayAdapter(this, R.layout.simple_dropdown_item_1line, skills)
            binding.etUpSkillId.setAdapter(skillTextAdapter)
        }
        liveData.getCompany().observe(this) {
            Log.d("ETAG", "onCreate: $it")
            company = it
            comTextAdapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, company)
            binding.etUpCompanyId.setAdapter(comTextAdapter)
        }
        binding.etUpSkillId.setOnItemClickListener { adapterView, view, i, l ->
            val skillName = skillTextAdapter.getItem(i).toString().lowercase()
            if (selectedSkills.contains(skillName)) {
                showToast("Already Added", this)
            } else {
                Repository.updateSkill("union", skillName)
                selectedSkills.add(skillName)
            }
            binding.etUpSkillId.text.clear()
            if (selectedSkills.size == 5) {
                binding.etUpSkillId.isEnabled = false
                binding.etUpSkillId.hint = "Remove one skill to enable"
                showToast("Max 5 allowed", this)
            }
            skillAdapter.notifyDataSetChanged()
        }
        binding.etUpCompanyId.setOnItemClickListener { _, _, i, _ ->
            val companyName = comTextAdapter.getItem(i).toString().lowercase()
            if (selectedCompany.contains(companyName)) {
                showToast("Already Added", this)
            } else {
                Repository.updateCompany("union", companyName)
                selectedCompany.add(companyName)
            }
            binding.etUpCompanyId.text.clear()
            if (selectedCompany.size == 5) {
                binding.etUpCompanyId.isEnabled = false
                binding.etUpCompanyId.hint = "Remove one company to enable"
                showToast("Max 5 allowed", this)
            }
            comAdapter.notifyDataSetChanged()
        }
        var uploadImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { imageRes: ActivityResult ->
                if (imageRes.resultCode == Activity.RESULT_OK) {
                    imageUri = Uri.parse(imageRes.data?.data.toString())
                    alertDialog.setTitle("Are you sure ?").setCancelable(false)
                        .setPositiveButton(
                            "Yes"
                        ) { di, _ ->
                            uploadProfilePicToFirestore()
                            di.dismiss()
                        }.setNegativeButton(
                            "No"
                        ) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                            imageUri = Uri.EMPTY
                        }.show()
                }
            }
        binding.ivUpProfilePicId.setOnClickListener {
            val intent = Intent()
            intent.type = ("image/*")
            intent.action = Intent.ACTION_GET_CONTENT
            uploadImage.launch(intent)
        }
        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            pdfUri = uri
            if (pdfUri != null) {
                alertDialog.setTitle("Are you sure ?").setCancelable(false)
                    .setPositiveButton(
                        "Yes"
                    ) { di, _ ->
                        uploadCVToFirestore()
                        di.dismiss()
                    }.setNegativeButton(
                        "No"
                    ) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        imageUri = Uri.EMPTY
                    }.show()
            }
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

        }
    }

    private fun uploadCVToFirestore() {
        pleasewaitdialog.setContentView(com.university_project.jobly.R.layout.progressbarlayout)
        pleasewaitdialog.setCancelable(false)
        pleasewaitdialog.show()
        val mstorageRef =
            storageRef.reference.child("cvPDF/${System.currentTimeMillis()}${Firebase.auth.uid}")
        mstorageRef.putFile(pdfUri)
            .addOnSuccessListener {
                mstorageRef.downloadUrl.addOnSuccessListener { cvUrl ->
                    if (cvUrl != null) {
                        updateProfileLiveData.updateProfile(cvUrl.toString(), "cvEmp")
                        pleasewaitdialog.dismiss()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    pleasewaitdialog.dismiss()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                pleasewaitdialog.dismiss()
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

    private fun uploadProfilePicToFirestore() {
        pleasewaitdialog.setContentView(com.university_project.jobly.R.layout.progressbarlayout)
        pleasewaitdialog.setCancelable(false)
        pleasewaitdialog.show()
        val storageRef =
            Firebase.storage.reference.child("profile/image/${System.currentTimeMillis()}")
        var link = "No Image"
        if (!Uri.EMPTY.equals(imageUri)) {
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
            val compressedImage = baos.toByteArray()
            storageRef.putBytes(compressedImage).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    link = uri.toString()
                    updateProfileLiveData.updateProfile(link, "profileImg")
                    pleasewaitdialog.dismiss()

                }
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                pleasewaitdialog.dismiss()
            }
        }
    }

    private fun showToast(s: String, context: Context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun onSkillDeleteClick(skill: String) {
        selectedSkills.remove(skill)
        Repository.updateSkill("remove", skill)
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

    private fun addCompany() {
//        val skillName = skillTextAdapter.getItem(i).toString().lowercase()
//        if (selectedSkills.contains(skillName)) {
//            showToast("Already Added", this)
//        } else {
//            Repository.updateSkill("union", skillName)
//            selectedSkills.add(skillName)
//        }
//        binding.etUpSkillId.text.clear()
//        if (selectedSkills.size == 5) {
//            binding.etUpSkillId.isEnabled = false
//            binding.etUpSkillId.hint = "Remove one skill to enable"
//            showToast("Max 5 allowed", this)
//        }
//        skillAdapter.notifyDataSetChanged()
    }

    override fun onCompDeleteClick(skill: String) {
        selectedCompany.remove(skill)
        Repository.updateCompany("remove", skill)
        skillAdapter.notifyDataSetChanged()
        binding.etUpCompanyId.isEnabled = true
        binding.etUpCompanyId.hint = "Add Company"
    }
}

