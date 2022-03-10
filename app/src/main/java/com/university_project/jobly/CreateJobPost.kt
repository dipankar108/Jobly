package com.university_project.jobly

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.adapter.SkillAdapter
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.databinding.ActivityCreateJobPostBinding
import com.university_project.jobly.datamodel.CallForInterViewDataModel
import com.university_project.jobly.datamodel.CreatePostModel
import com.university_project.jobly.interfaces.SkillClick

class CreateJobPost : AppCompatActivity(), SkillClick {
    lateinit var auth: FirebaseAuth
    val TAG = "TAG"
    private val genderList= listOf("Any","Male","Female")
    private lateinit var liveData: BaseViewModel
    private var skills = listOf<String>()
    private var selectedSkills = mutableListOf<String>()
    private lateinit var skillAdapter: ArrayAdapter<String>
    private lateinit var binding: ActivityCreateJobPostBinding
    val rvskillAdapter = SkillAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateJobPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.rvSkillViewId.layoutManager = LinearLayoutManager(this).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.rvSkillViewId.adapter = rvskillAdapter
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        liveData.getSkill().observe(this, { skill ->
            skills = skill
            skillAdapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, skills)
            binding.amEtSkillCreatePostId.setAdapter(skillAdapter)
        })
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
        val spinnerGenderAdapter=ArrayAdapter(
            this,android.R.layout.simple_spinner_dropdown_item,genderList
        )
        binding.spGenderCreatePostId.adapter=spinnerGenderAdapter
        binding.btnCreatePostId.setOnClickListener {
            val postTitle = binding.etTitleCreatePostId.text.toString()
            val postDesc = binding.etDescCratePostId.text.toString()
            val postExperience = Integer.parseInt(binding.etRegExperienceId.text.toString())
            val postSkills = selectedSkills
            val postSalary = binding.etRegSalaryId.text.toString().toInt()
            val postLocation = binding.etRegLocationId.text.toString()
            val postGender = binding.spGenderCreatePostId.selectedItem.toString()
            val attachmentLink = ""
            val postCompany = ""
            val timeStamp = System.currentTimeMillis()
            val callforinterview: ArrayList<CallForInterViewDataModel> = ArrayList()
            val isLike: ArrayList<String> = ArrayList()
            liveData.makePost(
                CreatePostModel(
                    Firebase.auth.uid.toString(),
                    postTitle,
                    postDesc,
                    postSkills,
                    postExperience,
                    postSalary,
                    postLocation,
                    emptyMap(),
                    attachmentLink,
                    timeStamp,
                    postCompany,
                    postGender,
                    callforinterview,
                    isLike
                )
            )
        }
        /**
        binding.btnCreatePostId.setOnClickListener {
        val postTitle = binding.etTitleCreatePostId.text.toString()
        val postDesc = binding.etDescCratePostId.text.toString()
        val postExperience = binding.etRegSalaryId.text.toString().toInt()
        val postSalary = binding.etRegSalaryId.text.toString().toInt()
        val postLocation = binding.etRegLocationId.text.toString()
        val postGender = binding.spGenderCreatePostId.selectedItem.toString()
        val attachmentLink = ""
        val timeStamp = System.currentTimeMillis()
        val callforinterview: ArrayList<CallForInterViewDataModel> = ArrayList()
        val isLike: ArrayList<String> = ArrayList()
        Firebase.firestore.collection("User").document(auth.uid.toString()).get()
        .addOnSuccessListener {
        val userInfo = it.data?.get("companyName") as String
        Firebase.firestore.collection("JobPost")
        .add(
        CreatePostModel(
        auth.uid.toString(),
        postTitle,
        postDesc,
        listOf(selectedSkills) as List<String>,
        postExperience,
        postSalary,
        postLocation,
        emptyMap(),
        attachmentLink,
        timeStamp,
        userInfo,
        postGender,
        callforinterview,
        isLike
        )
        ).addOnSuccessListener {
        Toast.makeText(this, "Post created", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ClientActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        }
        }


        db.get().addOnSuccessListener { results ->
        for (doc in results) {
        arrayListCategory.add(doc.id)
        }
        arrayListCategory.sorted()
        arrayListCategory[arrayListCategory.indexOf("0Select Any Category")] =
        "Select Any Category"
        val categoryAdapter = ArrayAdapter(
        this,
        android.R.layout.simple_spinner_dropdown_item,
        arrayListCategory
        )
        // binding.spCatagoryCreatePostId.adapter = categoryAdapter
        }

        binding.spCatagoryCreatePostId.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        subArray.clear()
        if (binding.spCatagoryCreatePostId.selectedItem.toString() != "Select Any Category") {
        binding.spSubCatagoryCreatePostId.isEnabled = true
        }
        val docRef =
        if (binding.spCatagoryCreatePostId.selectedItem.toString() == "Select Any Category") "0Select Any Category" else binding.spCatagoryCreatePostId.selectedItem.toString()
        db.document(docRef).get()
        .addOnSuccessListener { result ->
        for (arr in result.data?.values!!) {
        subArray.add(arr.toString())
        }
        val subCategoryAdapter = ArrayAdapter(
        this@CreateJobPost,
        R.layout.support_simple_spinner_dropdown_item,
        subArray.sorted()
        )
        binding.spSubCatagoryCreatePostId.adapter = subCategoryAdapter
        }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
        }

        //Getting gender from the database
        Firebase.firestore.collection("Gender").document(
        "0OF5b8M1XnLYTOJS6TDE"
        ).get().addOnSuccessListener {
        val genderList = ArrayList<String>()
        for (gender in it.data?.values!!) {
        genderList.add(gender.toString())
        }
        genderList.sorted()
        genderList[genderList.indexOf("0Select any gender")] = "Select any gender"
        val madapter =
        ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderList)
        binding.spGenderCreatePostId.adapter = madapter
        binding.spGenderCreatePostId.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (binding.spGenderCreatePostId.selectedItem.toString() != "Select any gender") {
        binding.etRegExperienceId.isEnabled = true
        binding.etRegSalaryId.isEnabled = true
        binding.etRegLocationId.isEnabled = true
        binding.btnCreatePostId.isEnabled = true
        }

        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
        }
        }

        binding.btnCreatePostId.setOnClickListener {
        val postTitle = binding.etTitleCreatePostId.text.toString()
        val postDesc = binding.etDescCratePostId.text.toString()
        val postCategory = binding.spCatagoryCreatePostId.selectedItem.toString()
        val postSubCategory = binding.spSubCatagoryCreatePostId.selectedItem.toString()
        val postExperience = binding.etRegSalaryId.text.toString().toInt()
        val postSalary = binding.etRegSalaryId.text.toString().toInt()
        val postLocation = binding.etRegLocationId.text.toString()
        val postGender = binding.spGenderCreatePostId.selectedItem.toString()
        val attachmentLink = ""
        val timeStamp = System.currentTimeMillis()
        val callforinterview: ArrayList<CallForInterViewDataModel> = ArrayList()
        val isLike: ArrayList<String> = ArrayList()
        Firebase.firestore.collection("User").document(auth.uid.toString()).get()
        .addOnSuccessListener {
        val userInfo = it.data?.get("companyName") as String
        Firebase.firestore.collection("JobPost")
        .add(
        CreatePostModel(
        auth.uid.toString(),
        postTitle,
        postDesc,
        arrayListOf(postCategory, postSubCategory),
        postExperience,
        postSalary,
        postLocation,
        emptyMap(),
        attachmentLink,
        timeStamp,
        userInfo,
        postGender,
        callforinterview,
        isLike
        )
        ).addOnSuccessListener {
        Toast.makeText(this, "Post created", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ClientActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        }
        }

        }

        }
         **/
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
}