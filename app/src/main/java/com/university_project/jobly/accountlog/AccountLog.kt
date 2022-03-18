package com.university_project.jobly.accountlog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.university_project.jobly.R
import com.university_project.jobly.databinding.ActivityAccountLogBinding


class AccountLog : AppCompatActivity() {
    lateinit var binding: ActivityAccountLogBinding
    private var insideLogin = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //   val getScreen = GetScreen(resources)
//        binding.frAccountFrameLayoutId.layoutParams.height =
//            GetAccountLogProperties.getFrameLayoutHeight(getScreen.getHeight())
        setFragment()
        binding.btnAccountCId.setOnClickListener {
            setFragment()
        }
    }

    private fun setFragment() {
        val loginfragment = LogInFragment()
        val createFragment = RegisterFragment()
        insideLogin = if (insideLogin) {
            binding.btnAccountCId.text="Create Account"
            supportFragmentManager.beginTransaction()
                .replace(R.id.fr_accountFrameLayout_id, loginfragment)
                .commit()
            false
        } else {
            binding.btnAccountCId.text="LogIn"
            supportFragmentManager.beginTransaction()
                .replace(R.id.fr_accountFrameLayout_id, createFragment).commit()
            true
        }
    }
}
