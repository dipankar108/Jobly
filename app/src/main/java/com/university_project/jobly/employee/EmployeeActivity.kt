package com.university_project.jobly.employee

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R
import com.university_project.jobly.databinding.ActivityEmployeeBinding
import com.university_project.jobly.utils.UtilClass

class EmployeeActivity : AppCompatActivity() {
    private val TAG = "EmployeeActivityP"

    private lateinit var binding: ActivityEmployeeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bnbEmpId.menu.findItem(R.id.e_allPost_menu_id).isChecked = true
        changedFragment(JobPostFragment())
        Log.d(TAG, "onCreate: ${Firebase.auth.uid.toString()}")
        binding.bnbEmpId.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.e_allPost_menu_id -> changedFragment(JobPostFragment())
                R.id.e_fabPost_menu_id -> changedFragment(FabPostFragment())
                R.id.e_appliedJob_menu_id -> changedFragment(AppliedPostFragment())
                R.id.e_callForInterview_menu_id -> changedFragment(EInterViewFragment())
            }
            return@setOnItemSelectedListener true
        }

    }

    private fun changedFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fl_emp_id, fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionsmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings_id -> Log.d(TAG, "onOptionsItemSelected: Settings menu clicked")
            R.id.menu_sign_out_id -> snout()
        }
        return true
    }

    private fun snout() {
        UtilClass.signOutNow(this, this, getSharedPreferences("userType", MODE_PRIVATE).edit())
    }
}