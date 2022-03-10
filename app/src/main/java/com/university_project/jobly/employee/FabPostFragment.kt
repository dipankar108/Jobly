package com.university_project.jobly.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.databinding.FragmentFabPostBinding
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.employee.adapter.PostAdapter
import com.university_project.jobly.employee.viewmodel.EmpViewModel

class FabPostFragment : Fragment(), ClickHandle {
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
        val postAdapter = PostAdapter(this)
        binding.rvEmpFabPostId.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEmpFabPostId.adapter = postAdapter
        liveData.getAllFabPost().observe(viewLifecycleOwner, {
            postAdapter.setDataToList(it)
            postAdapter.notifyDataSetChanged()
        })
    }

    override fun onLikeClick(postDataModel: PostDataModel, b: Boolean) {
        //Updating like or fab post
        liveData.updateLike(postDataModel.docId, Firebase.auth.uid.toString(), b)
    }
}