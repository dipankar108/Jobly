package com.university_project.jobly.employee

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.databinding.FragmentAppliedPostBinding
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.employee.adapter.PostAdapter
import com.university_project.jobly.interfaces.ClickHandle

class AppliedPostFragment : Fragment(), ClickHandle {
    private lateinit var _binding: FragmentAppliedPostBinding
    private val binding get() = _binding
    private lateinit var liveData: BaseViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppliedPostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        val postAdapter = PostAdapter(this)
        binding.rvEmpAppliedPostId.layoutManager = LinearLayoutManager(requireContext())
         binding.rvEmpAppliedPostId.adapter = postAdapter
        liveData.getMyApplication().observe(viewLifecycleOwner, { application ->
            Log.d("TAG", "onViewCreated: $application")
            postAdapter.setDataToList(application)
           postAdapter.notifyDataSetChanged()
        })
    }

    override fun onLikeClick(postDataModel: PostDataModel, b: Boolean) {

    }

    override fun onDescClick(docId: String) {

    }

    override fun onDeleteClick(docId: String) {

    }
}