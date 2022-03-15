package com.university_project.jobly.employee

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.JobPostView
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.databinding.FragmentJobPostBinding
import com.university_project.jobly.datamodel.PostDataModel
import com.university_project.jobly.employee.adapter.PostAdapter
import com.university_project.jobly.employee.viewmodel.EmpViewModel
import com.university_project.jobly.interfaces.ClickHandle

class JobPostFragment : Fragment(), ClickHandle {
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
        val myAdapter = PostAdapter(this)
        binding.rvEmpJobPostViewId.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEmpJobPostViewId.adapter = myAdapter
       // Repository.getChatList(Firebase.auth.uid.toString(),"")
        liveData = ViewModelProvider(this)[EmpViewModel::class.java]
      liveData.getMYSkill().observe(viewLifecycleOwner,{
          liveData.getJobPost(it).observe(viewLifecycleOwner, { list ->
              myAdapter.setDataToList(list.sortedByDescending { it.timeStamp })
              myAdapter.notifyDataSetChanged()
          })
      })
    }

    override fun onLikeClick(postDataModel: PostDataModel, b: Boolean) {
        liveData.updateLike(postDataModel.docId, Firebase.auth.uid.toString(), b)
    }

    override fun onDescClick(docId: String) {
        val intent = Intent(context, JobPostView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("docId", docId)
        startActivity(intent)
    }

    override fun onDeleteClick(docId: String) {
    }

}