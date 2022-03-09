package com.university_project.jobly.employee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.university_project.jobly.R
import com.university_project.jobly.client.fragment.ClientCallForInterViewFragment
import com.university_project.jobly.databinding.ActivityEmployeeBinding

class EmployeeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEmployeeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bnbEmpId.menu.findItem(R.id.e_allPost_menu_id).isChecked=true
        changedFragment(JobPostFragment())
        binding.bnbEmpId.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.e_allPost_menu_id->changedFragment(JobPostFragment())
                R.id.e_fabPost_menu_id->changedFragment(FabPostFragment())
                R.id.e_appliedJob_menu_id->changedFragment(AppliedPostFragment())
                R.id.e_callForInterview_menu_id->changedFragment(EInterViewFragment())
            }
            return@setOnItemSelectedListener true
        }

    }
    private fun changedFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fl_emp_id,fragment).commit()
    }
}