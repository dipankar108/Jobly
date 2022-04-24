package com.university_project.jobly.employee

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R
import com.university_project.jobly.accountlog.UpdateProfileActivity
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.chatserver.InterViewFragment
import com.university_project.jobly.databinding.ActivityEmployeeBinding
import com.university_project.jobly.utils.ChangePassword
import com.university_project.jobly.utils.SharedInfo
import com.university_project.jobly.utils.UtilClass
import kotlin.system.exitProcess

class EmployeeActivity : AppCompatActivity() {
    private val TAG = "EmployeeActivityP"
    private lateinit var dialog: Dialog
    private lateinit var binding: ActivityEmployeeBinding
    private lateinit var buttonSubmit: Button
    private lateinit var oldPass: EditText
    private lateinit var newPass: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater)
        val actionbar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#79AA8D"))
        actionbar?.setBackgroundDrawable(colorDrawable)
        setContentView(binding.root)
        dialog = Dialog(this)
        binding.bnbEmpId.menu.findItem(R.id.e_allPost_menu_id).isChecked = true
        changedFragment(JobPostFragment())
        Repository.updateActiveStatus(true)
//        buttonSubmit = findViewById(R.id.btn_submitPass_update_id)
//        oldPass = findViewById(R.id.et_OldPass_Update_id)
//        newPass = findViewById(R.id.et_newPassword_Update_id)
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
            R.id.menu_pass_id ->
                ChangePassword.changePassword(this)
            R.id.menu_sign_out_id -> snout()
            R.id.menu_updateProfile_id -> updateProfile()
        }
        return true
    }

    private fun updateProfile() {
        startActivity(Intent(this, UpdateProfileActivity::class.java))
    }

    private fun snout() {
        viewModelStore.clear()
        UtilClass.signOutNow(
            this,
            this,
            getSharedPreferences(SharedInfo.USER.user, MODE_PRIVATE).edit()
        )
    }

    override fun onBackPressed() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                exitProcess(0)
            }
            .setNegativeButton("No") { di, _ ->
                di.dismiss()
            }.show()
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