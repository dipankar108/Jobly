package com.university_project.jobly.employee

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R
import com.university_project.jobly.baseviewmodel.BaseViewModel
import com.university_project.jobly.baseviewmodel.Repository
import com.university_project.jobly.baseviewmodel.profile.UserViewModel
import com.university_project.jobly.chatserver.InterViewFragment
import com.university_project.jobly.databinding.ActivityEmployeeBinding
import com.university_project.jobly.utils.SharedInfo
import com.university_project.jobly.utils.UpdatePassword
import com.university_project.jobly.utils.UtilClass
import kotlin.system.exitProcess

class EmployeeActivity : AppCompatActivity() {
    private val TAG = "EmployeeActivityP"
    private lateinit var userLiveData: UserViewModel
    private lateinit var dialog: Dialog
    private lateinit var binding: ActivityEmployeeBinding
    private lateinit var liveData: BaseViewModel
    private var userType: String = ""
    private lateinit var bandial: AlertDialog.Builder
    private var pdfUri = Uri.EMPTY
    private lateinit var mdialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater)
        val actionbar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#79AA8D"))
        actionbar?.setBackgroundDrawable(colorDrawable)
        setContentView(binding.root)
        dialog = Dialog(this)
        userLiveData = ViewModelProvider(this)[UserViewModel::class.java]
        bandial = AlertDialog.Builder(this)
        binding.bnbEmpId.menu.findItem(R.id.e_allPost_menu_id).isChecked = true
        changedFragment(JobPostFragment())
        Repository.updateActiveStatus(true)
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
        liveData = ViewModelProvider(this)[BaseViewModel::class.java]
        userType = getSharedPreferences(
            SharedInfo.USER.user,
            MODE_PRIVATE
        ).getString(SharedInfo.USER_TYPE.user, null).toString()
        mdialog = Dialog(this)
        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                pdfUri = uri
                if (pdfUri != null) {
                    Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                }
            }
        userLiveData.userBanInfo.observe(this) {
            if (it) {
                Toast.makeText(
                    this,
                    "You are banned from using our service.Please contact with us If you think we're made wrong decision.Thank you",
                    Toast.LENGTH_LONG
                ).show()
                bandial.setPositiveButton(
                    "Contact Us"
                ) { _, _ ->
                    latestExampleEmailCreation(
                        arrayOf("dipankar0debnath@gmail.com"),
                        "Ban Issue",
                        "Please dont edit this id: ${Firebase.auth.uid.toString()}"
                    )
                }.setCancelable(false).show()
            }
        }
        liveData.isVerified(userType).observe(this) {
            Log.d(TAG, "onCreate: $it")
            if (!it) {
                val view = layoutInflater.inflate(R.layout.verification, null, false)
                mdialog.setContentView(view)
                mdialog.show()
                mdialog.setCancelable(false)
                val uploadPDFBtn: Button = view.findViewById(R.id.btn_uploadPdfButtonVer_Id)
                val sendProf: Button = view.findViewById(R.id.btn_sendPdfButtonVer_Id)
                uploadPDFBtn.setOnClickListener {
                    getContent.launch("application/pdf")
                }
                sendProf.setOnClickListener {
                    if (pdfUri != null) {
                        liveData.setVerificationFile(pdfUri, userType, this)
                    }
                }
            } else {
                mdialog.dismiss()
            }
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
            R.id.menu_pass_id -> {
                val updatePassword = UpdatePassword(this, layoutInflater)
                updatePassword.showDialog()
            }
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

    private fun latestExampleEmailCreation(
        addresses: Array<String>, subject: String, text: String
    ) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
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