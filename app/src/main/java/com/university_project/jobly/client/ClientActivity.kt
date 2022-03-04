package com.university_project.jobly.client
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.university_project.jobly.CreateJobPost
import com.university_project.jobly.R
import com.university_project.jobly.client.clientviewmodel.ClientPostViewModel
import com.university_project.jobly.client.fragment.ClientAppliedFragment
import com.university_project.jobly.client.fragment.ClientCallForInterViewFragment
import com.university_project.jobly.client.fragment.ClientJobPostFragment
import com.university_project.jobly.databinding.ActivityClientBinding
class ClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientBinding
    private lateinit var liveDataModel: ClientPostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeFragment(ClientJobPostFragment())
        binding.bmnClientNavBar.menu.findItem(R.id.client_myPost_menu_id).isChecked = true
        binding.bmnClientNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.client_myPost_menu_id -> changeFragment(ClientJobPostFragment())
                R.id.client_applied_menu_id -> changeFragment(ClientAppliedFragment())
                R.id.client_callForInterview_menu_id -> changeFragment(
                    ClientCallForInterViewFragment()
                )
            }
            return@setOnItemSelectedListener true
        }

        binding.fabMainId.setOnClickListener {
            val intent = Intent(this, CreateJobPost::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fl_clientLayout_id, fragment)
            .commit()
    }

}