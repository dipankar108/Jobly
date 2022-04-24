package com.university_project.jobly.client

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.CreateJobPost
import com.university_project.jobly.R
import com.university_project.jobly.UpdateClientProfile
import com.university_project.jobly.accountlog.AccountLog
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.baseviewmodel.profile.UserViewModel
import com.university_project.jobly.chatserver.InterViewFragment
import com.university_project.jobly.client.fragment.ClientAppliedFragment
import com.university_project.jobly.client.fragment.ClientJobPostFragment
import com.university_project.jobly.databinding.ActivityClientBinding
import com.university_project.jobly.utils.ChangePassword
import com.university_project.jobly.utils.SharedInfo
import com.university_project.jobly.utils.UtilClass
import kotlin.system.exitProcess

class ClientActivity : AppCompatActivity() {
    private val TAG = "ClientActivity"
    private lateinit var userLiveData: UserViewModel
    private lateinit var binding: ActivityClientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        val actionbar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#79AA8D"))
        actionbar?.setBackgroundDrawable(colorDrawable)
        setContentView(binding.root)
        changeFragment(ClientJobPostFragment())
        Firebase.auth.uid!!
        Repository.updateActiveStatus(true)
        userLiveData = ViewModelProvider(this)[UserViewModel::class.java]
        userLiveData.userBanInfo.observe(this) {
            if (it) {
                Toast.makeText(
                    this,
                    "You are banned from using our service.Please contact with us If you think we're made wrong decision.Thank you",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this, AccountLog::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }
        binding.bmnClientNavBar.menu.findItem(R.id.client_myPost_menu_id).isChecked = true
        binding.bmnClientNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.client_myPost_menu_id -> changeFragment(ClientJobPostFragment())
                R.id.client_applied_menu_id -> changeFragment(ClientAppliedFragment())
                R.id.client_callForInterview_menu_id -> changeFragment(
                    InterViewFragment()
                )
            }
            return@setOnItemSelectedListener true
        }

        binding.fabMainId.setOnClickListener {
            val intent = Intent(this, CreateJobPost::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fl_clientLayout_id, fragment)
            .commit()
    }

    override fun onBackPressed() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                exitProcess(0)
            }
            .setNegativeButton("No") { di, _ ->
                di.dismiss()
            }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionsmenu, menu)
        return true
    }

    override fun onRestart() {
        super.onRestart()
        Repository.updateActiveStatus(true)
    }

    override fun onPause() {
        super.onPause()
        Repository.updateActiveStatus(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_pass_id -> ChangePassword.changePassword(this)
            R.id.menu_sign_out_id -> {
                viewModelStore.clear()
                UtilClass.signOutNow(
                    this,
                    this,
                    getSharedPreferences(SharedInfo.USER.user, MODE_PRIVATE).edit()
                )
            }
            R.id.menu_updateProfile_id -> {
                startActivity(Intent(this, UpdateClientProfile::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
        }
        return true
    }

    private fun requireContext(): Context {
        return this@ClientActivity
    }
}