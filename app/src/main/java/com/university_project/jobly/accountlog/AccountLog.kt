package com.university_project.jobly.accountlog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.university_project.jobly.R
import com.university_project.jobly.databinding.ActivityAccountLogBinding


class AccountLog : AppCompatActivity() {
    lateinit var binding: ActivityAccountLogBinding
    private var insideLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        binding = ActivityAccountLogBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFragment()
        binding.btnAccountCId.setOnClickListener {
            setFragment()
        }
        binding.btnAccountContactId.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:dipankar0debnath@gmail.com")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    private fun setFragment() {
        val loginfragment = LogInFragment()
        val createFragment = RegisterFragment()
        insideLogin = if (insideLogin) {
            binding.btnAccountCId.text = "Create Account"
            supportFragmentManager.beginTransaction()
                .replace(R.id.fr_accountFrameLayout_id, loginfragment)
                .commit()
            false
        } else {
            binding.btnAccountCId.text = "LogIn"
            supportFragmentManager.beginTransaction()
                .replace(R.id.fr_accountFrameLayout_id, createFragment).commit()
            true
        }
    }
}
