package com.university_project.jobly.accountlog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R
import com.university_project.jobly.client.ClientActivity
import com.university_project.jobly.databinding.FragmentLogInBinding
import com.university_project.jobly.employee.EmployeeActivity
import com.university_project.jobly.utils.SharedInfo

class LogInFragment : Fragment() {
    private lateinit var _binding: FragmentLogInBinding
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreateView: On Fragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val resetDialog = Dialog(view.context)
        val dialog = Dialog(view.context)
        val dview = layoutInflater.inflate(R.layout.progressbarlayout, null, false)
        val resetView = layoutInflater.inflate(R.layout.updateprofiledialog, null, false)
        binding.tvResetPasswordId.setOnClickListener {
            resetDialog.setContentView(resetView)
            resetDialog.show()
            val title: TextView = resetView.findViewById(R.id.tv_titleview_update_id)
            val inputEmail: EditText = resetView.findViewById(R.id.et_bottomFragment_Update_id)
            val buttonSubmit: Button = resetView.findViewById(R.id.btn_submit_update_id)
            title.text = "Update Password"
            inputEmail.hint = "Enter your email here"
            buttonSubmit.setOnClickListener {
                auth.sendPasswordResetEmail(inputEmail.text.toString())
                    .addOnCompleteListener { task ->
                        inputEmail.text.clear()
                        resetDialog.dismiss()
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Reset link send on your email",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else {
                            Toast.makeText(context, task.exception?.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }
        }
        dialog.setContentView(dview)
        auth = Firebase.auth
        binding.btnLogSubmitId.setOnClickListener {
            dialog.show()
            val userEmail = binding.etLogEmailId.text.toString()
            val userPass = binding.etLogPassId.text.toString()
            if (userEmail.isEmpty() || userPass.isEmpty()) {
                dialog.dismiss()
                Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(userEmail, userPass).addOnSuccessListener {
                    val currentUser = auth.currentUser
                    currentUser?.let { mcurrentUser ->
                        if (!mcurrentUser.isEmailVerified) {
                            Firebase.firestore.collection("User")
                                .document(Firebase.auth.uid.toString())
                                .get().addOnSuccessListener {
                                    val userType = it.data!!["userType"]
                                    val sh = activity?.getSharedPreferences(
                                        SharedInfo.USER.user,
                                        AppCompatActivity.MODE_PRIVATE
                                    )
                                    val editor = sh?.edit()
                                    editor?.putString(
                                        SharedInfo.USER_TYPE.user,
                                        userType.toString()
                                    )
                                        ?.apply()
                                    changeActivity(userType, dialog)
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                        } else {
                            Toast.makeText(
                                context,
                                "Please verify your email first",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }.addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }
    }

    private fun changeActivity(userType: Any?, dialog: Dialog) {

        if (userType == "Client") {
            dialog.dismiss()
            val intent = Intent(requireContext(), ClientActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            activity?.finish()
        } else {
            dialog.dismiss()
            val intent = Intent(requireContext(), EmployeeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            activity?.finish()
        }
    }
}