package com.university_project.jobly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.accountlog.AccountLog
import com.university_project.jobly.client.ClientActivity
import com.university_project.jobly.utils.GetTheme
import com.university_project.jobly.utils.screensize.GetScreen
import com.university_project.jobly.utils.screensize.SplashScreenSize
import com.university_project.jobly.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.*


class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    val TAG = "TAG"
    val auth = Firebase.auth
    val context = this@SplashScreen
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
        Log.d(TAG, "onCreate: " + auth.uid)
        GlobalScope.launch {
            delay(2000)
            if (auth.uid != null) {
                Firebase.firestore.collection("User").document(auth.uid.toString()).get()
                    .addOnSuccessListener {
                        if (it.data != null) {
                            val intent = Intent(context, ClientActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        } else {
                            auth.signOut()
                            startActivity(Intent(this@SplashScreen, AccountLog::class.java))
                        }

                    }

            } else {
                startActivity(Intent(this@SplashScreen, AccountLog::class.java))
            }
            
        }
    }
}