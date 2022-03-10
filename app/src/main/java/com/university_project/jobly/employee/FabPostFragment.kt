package com.university_project.jobly.employee

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.university_project.jobly.databinding.FragmentFabPostBinding
import com.university_project.jobly.employee.viewmodel.EmpViewModel

class FabPostFragment : Fragment() {
    private lateinit var _binding: FragmentFabPostBinding
    private lateinit var liveData: EmpViewModel
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFabPostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        liveData = ViewModelProvider(this)[EmpViewModel::class.java]
        liveData.getAllFabPost().observe(viewLifecycleOwner,{

        })
    }
}