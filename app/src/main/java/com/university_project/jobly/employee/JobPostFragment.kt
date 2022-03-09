package com.university_project.jobly.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.university_project.jobly.databinding.FragmentJobPostBinding
import com.university_project.jobly.employee.adapter.EmpJobPostAdapter
import com.university_project.jobly.employee.viewmodel.EmpViewModel

class JobPostFragment : Fragment() {
    private val TAG = "JobPostFragmentP"
    private lateinit var liveData: EmpViewModel
    private lateinit var _binding: FragmentJobPostBinding
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobPostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myAdapter = EmpJobPostAdapter()
        binding.rvEmpJobPostViewId.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEmpJobPostViewId.adapter = myAdapter
        liveData = ViewModelProvider(this)[EmpViewModel::class.java]
        liveData.getJobPost(listOf("Medical")).observe(viewLifecycleOwner, {
            myAdapter.setDataToList(it)
            myAdapter.notifyDataSetChanged()

        })

    }
}