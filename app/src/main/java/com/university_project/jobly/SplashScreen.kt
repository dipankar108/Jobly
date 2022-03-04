package com.university_project.jobly

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.accountlog.AccountLog
import com.university_project.jobly.client.ClientActivity
import com.university_project.jobly.databinding.ActivitySplashScreenBinding
import com.university_project.jobly.utils.GetTheme
import com.university_project.jobly.utils.screensize.GetScreen
import com.university_project.jobly.utils.screensize.SplashScreenSize
import kotlinx.coroutines.*


class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    val TAG = "TAG"
    val auth = Firebase.auth
    val context = this@SplashScreen
    lateinit var sh: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        if (GetTheme.getDarkTheme(resources)) setTheme(R.style.Theme_SplashJoblyDark)
        else setTheme(R.style.Theme_SplashJoblyLight)
        setContentView(binding.root)
        val getScreen = GetScreen(resources)
        binding.progressBar.layoutParams.height =
            SplashScreenSize.getProgressbarSize(getScreen.getGeneralDp()).toInt()
        binding.progressBar.layoutParams.width =
            SplashScreenSize.getProgressbarSize(getScreen.getGeneralDp()).toInt()
        binding.progressBar.requestLayout()
        sh = getSharedPreferences("userType", MODE_PRIVATE)
        val editor = sh.edit()
        if (auth.uid != null) {
            if (sh.getString("m_userType", null) == null) {
                Firebase.firestore.collection("User").document(auth.uid.toString()).get()
                    .addOnSuccessListener {
                        if (it.data != null) {
                            val userinfo = it.data!!["userType"].toString()
                            editor.putString("m_userType", userinfo)
                            changeActivity(userinfo)
                        } else {
                            auth.signOut()
                            startActivity(Intent(this@SplashScreen, AccountLog::class.java))
                        }

                    }


            } else {
                GlobalScope.launch {
                    delay(500)
                    sh.getString("m_userType", null)?.let { changeActivity(it) }
                }
            }
        } else {
            startActivity(Intent(this@SplashScreen, AccountLog::class.java))
            finish()
        }
    }

    private fun changeActivity(userInfo: String) {
        if (userInfo == "Client") {
            val intent = Intent(context, ClientActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(context, EmployeeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }
}