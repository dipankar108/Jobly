package com.university_project.jobly
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.university_project.jobly.databinding.ActivityJobPostViewBinding

class JobPostView : AppCompatActivity() {
    private lateinit var binding:ActivityJobPostViewBinding
    lateinit var docID:String
    val TAG="TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityJobPostViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle=intent.extras
        docID=bundle!!.getString("docId").toString()
        Log.d(TAG, "onCreate: "+docID)
    }
}