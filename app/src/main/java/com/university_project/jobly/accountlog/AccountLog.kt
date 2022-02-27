package com.university_project.jobly.accountlog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.university_project.jobly.R
import com.university_project.jobly.databinding.ActivityAccountLogBinding
import com.university_project.jobly.utils.screensize.GetAccountLogProperties
import com.university_project.jobly.utils.screensize.GetScreen


class AccountLog : AppCompatActivity() {
    lateinit var binding: ActivityAccountLogBinding
    var insideLogin = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val getScreen = GetScreen(resources)
        binding.frAccountFrameLayoutId.layoutParams.height =
            GetAccountLogProperties.getFrameLayoutHeight(getScreen.getHeight())
        setFragment()
        binding.btnAccountCId.setOnClickListener {
            setFragment()
        }
    }
    private fun setFragment() {
        var loginfragment = LogInFragment()
        var createFragment = RegisterFragment()
        insideLogin = if (insideLogin) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fr_accountFrameLayout_id, loginfragment)
                .commit()
            false
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fr_accountFrameLayout_id, createFragment).commit()
            true
        }
    }
}
