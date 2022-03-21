package com.university_project.jobly.employee

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R
import com.university_project.jobly.accountlog.UpdateProfileActivity
import com.university_project.jobly.chatserver.InterViewFragment
import com.university_project.jobly.databinding.ActivityEmployeeBinding
import com.university_project.jobly.utils.SharedInfo
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
                R.id.e_callForInterview_menu_id -> changedFragment(InterViewFragment())
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
            R.id.menu_updateProfile_id -> updateProfile()
        }
        return true
    }

    private fun updateProfile() {
        startActivity(Intent(this, UpdateProfileActivity::class.java))
    }

    private fun snout() {
        UtilClass.signOutNow(
            this,
            this,
            getSharedPreferences(SharedInfo.USER.user, MODE_PRIVATE).edit()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Destroyed Employee Activity")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onDestroy: Pause Employee Activity")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Restarted")
    }
}