package com.university_project.jobly.accountlog

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R
import com.university_project.jobly.adapter.SkillAdapter
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.client.ClientActivity
import com.university_project.jobly.databinding.FragmentRegisterBinding
import com.university_project.jobly.datamodel.ClientProfileModel
import com.university_project.jobly.datamodel.EmployeeProfileModel
import com.university_project.jobly.employee.EmployeeActivity
import com.university_project.jobly.interfaces.SkillClick
import com.university_project.jobly.utils.GetTheme
import com.university_project.jobly.utils.screensize.GetScreen
import com.university_project.jobly.utils.screensize.RegisterFragmentSize

class RegisterFragment : Fragment(R.layout.fragment_register), SkillClick {
    private lateinit var auth: FirebaseAuth
    private lateinit var liveData: BaseViewModel
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val selectedSkills = mutableListOf<String>()
    private var skills = listOf<String>()
    private lateinit var skillAdapter: ArrayAdapter<String>
    private lateinit var rvskillAdapter: SkillAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getScreen = GetScreen(resources)
        val gdp = getScreen.getGeneralDp()
        val dialog = Dialog(requireContext())
        binding.tvRegNameId.width = RegisterFragmentSize.getRegisterNameWidth(gdp)
        binding.tvRegNameId.height = RegisterFragmentSize.getRegisterNameHeight(gdp)
        binding.tvRegNameId.textSize = RegisterFragmentSize.getRegFontSize(gdp)
        binding.tvRegNameId.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                getTextColor(resources)
            )
        )
        binding.tvRegNameId.setMargin()
        rvskillAdapter = SkillAdapter(this)
        val height = RegisterFragmentSize.getRegEditHeght(gdp)
//        val color = ContextCompat.getColor(requireContext(), R.color.white)
        binding.etRegEmailId.height = height
        binding.etRegFnameId.height = height
        binding.etRegLNameId.height = height
        binding.etRegPassId.height = height
        binding.rvRegSkillViewId.layoutManager = LinearLayoutManager(requireContext()).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.rvRegSkillViewId.adapter = rvskillAdapter
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.userTypeArray,
            android.R.layout.simple_spinner_item,
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }
        binding.rvRegSkillViewId.layoutManager = LinearLayoutManager(requireContext()).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        liveData.getSkill().observe(viewLifecycleOwner, {
            skills = it
            skillAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, skills)
            binding.amEtRegSkillCreatePostId.setAdapter(skillAdapter)

        })
        binding.amEtRegSkillCreatePostId.setOnItemClickListener { _, _, i, _ ->
            val skillName = skillAdapter.getItem(i).toString().lowercase()
            if (selectedSkills.contains(skillName)) {
                Toast.makeText(requireContext(), "Already Added", Toast.LENGTH_SHORT).show()
            } else selectedSkills.add(skillName)
            binding.amEtRegSkillCreatePostId.text.clear()
            if (selectedSkills.size == 5) {
                binding.amEtRegSkillCreatePostId.isEnabled = false
                binding.amEtRegSkillCreatePostId.hint = "Remove one skill to enable"
                Toast.makeText(requireContext(), "Max 5 allowed", Toast.LENGTH_SHORT).show()
            }
            rvskillAdapter.setList(selectedSkills)
            rvskillAdapter.notifyDataSetChanged()
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                if (adapterView?.getItemAtPosition(position) == "Client") {
                    binding.amEtRegSkillCreatePostId.visibility = GONE
                } else {
                    binding.amEtRegSkillCreatePostId.visibility = VISIBLE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        binding.btnRegSubmitId.setOnClickListener {

            val fName = convertToString(binding.etRegFnameId.text)
            val lName = convertToString(binding.etRegLNameId.text)
            val userPass = convertToString(binding.etRegPassId.text)
            val pass = convertToString(binding.etRegPassId.text)
            val userEmail = convertToString(binding.etRegEmailId.text)
            val userType = convertToString(binding.spinner.selectedItem)
            if (fName.isEmpty() || lName.isEmpty() || userPass.isEmpty() || pass.isEmpty() || userEmail.isEmpty() || userType == "Select" || selectedSkills.isEmpty()
            ) {
                Toast.makeText(requireContext(), "Please fill every field", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (userPass == pass) {

                    dialog.setContentView(R.layout.progressbarlayout)
                    dialog.show()
                      createAccount(
                    userEmail,
                    userPass,
                    fName,
                    lName,
                    userType,
                    dialog,
                    selectedSkills
                    )
                }
            }
        }
    }

    private fun createAccount(
        userEmail: String,
        userPass: String,
        fName: String,
        lName: String,
        userType: String,
        dialog: Dialog,
        selectedSkill: MutableList<String>
    ) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val db = Firebase.firestore.collection("User")
                if (userType == "Client") {
                    val user =
                        ClientProfileModel(
                            auth.uid.toString(), fName, lName, userPass, userEmail, userType, "",
                            ArrayList(), "", "",
                            verify = false,
                            banned = false,
                            false,
                            "clientprofileurl"
                        )
                    db.document(auth.uid.toString()).set(user)
                        .addOnSuccessListener {
                            binding.pbRegId.visibility = GONE

                            startActivity(Intent(requireContext(), ClientActivity::class.java))
                        }
                } else {
                    val user =
                        EmployeeProfileModel(
                            auth.uid.toString(),
                            fName,
                            lName,
                            userEmail,
                            userPass,
                            userType,
                            selectedSkill as ArrayList<String>,
                            "",
                            "",
                            "",
                            verify = false,
                            banned = false,
                            ""
                        )
                    db.document(auth.uid.toString()).set(user)
                        .addOnSuccessListener {
                            dialog.dismiss()
                            startActivity(Intent(requireContext(), EmployeeActivity::class.java))
                        }
                }


            }
        }.addOnFailureListener {
            binding.pbRegId.visibility = GONE
            Log.d("TAG", "createAccount: " + it.message)
            Toast.makeText(
                requireContext(),
                it.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSkillDeleteClick(skill: String) {
        selectedSkills.remove(skill)
        rvskillAdapter.notifyDataSetChanged()
        binding.amEtRegSkillCreatePostId.isEnabled = true
        binding.amEtRegSkillCreatePostId.hint = "Add Skill"
    }
}

private fun convertToString(convertibleString: Any): String {
    return convertibleString.toString()
}

private fun getTextColor(resources: Resources): Int {
    if (GetTheme.getDarkTheme(resources)) return R.color.white
    return R.color.black
}

private fun View.setMargin() {
    top = 5
    bottom = 10
}
