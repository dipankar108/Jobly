package com.university_project.jobly.client.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.university_project.jobly.client.AppliedEmployeeActivity
import com.university_project.jobly.client.adapter.AppliedViewAdapter
import com.university_project.jobly.client.clientviewmodel.ClientPostViewModel
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.client.interfaces.AppliedClickedHandle
import com.university_project.jobly.databinding.FragmentClientAppliedBinding

class ClientAppliedFragment : Fragment(), AppliedClickedHandle {
    private lateinit var _binding: FragmentClientAppliedBinding
    private val binding get() = _binding
    private val TAG = "ClientAppliedFragment"
    private lateinit var liveDataModel: ClientPostViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClientAppliedBinding.inflate(layoutInflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvClientAppliedId.layoutManager = LinearLayoutManager(requireContext())
        val appliedViewAdapter = AppliedViewAdapter(this)
        binding.rvClientAppliedId.adapter = appliedViewAdapter
        liveDataModel = ViewModelProvider(requireActivity())[ClientPostViewModel::class.java]
        setAppliedPostToRecyclerView(appliedViewAdapter)
    }

    private fun setAppliedPostToRecyclerView(appliedViewAdapter: AppliedViewAdapter) {
        liveDataModel.postList.observe(viewLifecycleOwner) {
            appliedViewAdapter.setArrayList(it)
            appliedViewAdapter.notifyDataSetChanged()
        }
    }

    override fun onAppliedClicked(postDataModel: PostDataModel) {
        val intent = Intent(requireContext(), AppliedEmployeeActivity::class.java).also {
            it.putExtra("docId", postDataModel.docId)
        }
        startActivity(intent)

    }
}