package com.university_project.jobly.accountlog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.MainActivity
import com.university_project.jobly.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {
    private lateinit var _binding: FragmentLogInBinding
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreateView: On Fragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        binding.btnLogSubmitId.setOnClickListener {
            val userEmail = binding.etLogEmailId.text.toString()
            val userPass = binding.etLogPassId.text.toString()
            if (userEmail.isEmpty() || userPass.isEmpty()) {
                Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(userEmail, userPass).addOnSuccessListener {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    activity?.finish()
                }.addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}