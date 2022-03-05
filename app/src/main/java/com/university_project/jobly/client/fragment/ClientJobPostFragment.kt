package com.university_project.jobly.client.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.JobPostView
import com.university_project.jobly.client.adapter.PostViewAdapter
import com.university_project.jobly.client.clientviewmodel.ClientPostViewModel
import com.university_project.jobly.client.datamodel.ClientPostDataModel
import com.university_project.jobly.client.interfaces.ClickHandle
import com.university_project.jobly.databinding.FragmentClientJobPostBinding

class ClientJobPostFragment : Fragment(), ClickHandle {
    private  val TAG = "ClientJobPostFragment"
    private lateinit var _binding: FragmentClientJobPostBinding
    private lateinit var liveDataModel: ClientPostViewModel
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientJobPostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        val postViewAdapter = PostViewAdapter(this)
        binding.rvClientPostViewId.layoutManager = LinearLayoutManager(requireContext())
        binding.rvClientPostViewId.adapter = postViewAdapter
        liveDataModel = ViewModelProvider(requireActivity())[ClientPostViewModel::class.java]
        if (liveDataModel.loading()) {
            binding.pbClientViewId.visibility = View.VISIBLE
        } else binding.pbClientViewId.visibility = View.GONE
        getMutableData(postViewAdapter)
//        binding.sflClientPostViewRefreshId.setOnRefreshListener {
//            Log.d("TAG", "onViewCreated: refreshing")
//           // myUpdateOperation();
//            postViewAdapter.notifyDataSetChanged()
//        }

    }

    private fun getMutableData(postViewAdapter: PostViewAdapter) {
        liveDataModel.postList.observe(viewLifecycleOwner, { list ->
            val sortedList = list.sortedByDescending { it.timeStamp }
            sortedList.forEach {
                Log.d(TAG, "getMutableData: ${it.timeStamp} ${it.title}")
            }
            postViewAdapter.setArrayList(sortedList)
            postViewAdapter.notifyDataSetChanged()
            binding.pbClientViewId.visibility = View.GONE
        })
    }

    override fun itemClicked(id: String, clientPostDataModel: ClientPostDataModel) {
        val intent = Intent(context, JobPostView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("docId", id)
        startActivity(intent)
    }

//    private suspend fun myUpdateOperation() {
//        liveDataModel.getLiveData()
//        binding.sflClientPostViewRefreshId.isRefreshing=false
//    }
}

//{
//        val db=Firebase.firestore.collection("JobPost")
//        val query=db.whereEqualTo("userId",auth.uid.toString())
//        query.get().addOnSuccessListener {
//            for (doc in it.documents){
//                //Log.d("TAG", "onViewCreated: $doc")
//                val createPostModel=doc.toObject(ClientPostDataModel::class.java)
//                Log.d("TAG", "onViewCreated: ${createPostModel?.desc}")
//            }
//        }
//}