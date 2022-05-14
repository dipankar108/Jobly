package com.university_project.jobly

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.accountlog.AccountLog
import com.university_project.jobly.client.ClientActivity
import com.university_project.jobly.databinding.ActivitySplashScreenBinding
import com.university_project.jobly.employee.EmployeeActivity
import com.university_project.jobly.utils.SharedInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    val TAG = "TAG"
    val auth = Firebase.auth
    val context = this@SplashScreen
    lateinit var sh: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        sh = getSharedPreferences(SharedInfo.USER.user, MODE_PRIVATE)
        val editor = sh.edit()
        if (auth.uid != null && auth.currentUser?.isEmailVerified == true) {
            if (sh.getString(SharedInfo.USER_TYPE.user, null) == null) {
                Firebase.firestore.collection("User").document(auth.uid.toString()).get()
                    .addOnSuccessListener {
                        if (it.data != null) {
                            val userinfo = it.data!!["userType"].toString()
                            editor.putString(SharedInfo.USER_TYPE.user, userinfo)
                            editor.apply()
                            changeActivity(userinfo)
                        } else {
                            auth.signOut()
                            editor.clear()
                            startActivity(Intent(this@SplashScreen, AccountLog::class.java))
                        }

                    }

            } else {
                sh.getString(SharedInfo.USER_TYPE.user, null)?.let {
                    Log.d(TAG, "onCreate: $it")
                    changeActivity(it)
                }
            }
        } else {
            GlobalScope.launch {
                delay(500)
                startActivity(Intent(this@SplashScreen, AccountLog::class.java))
                finish()
            }
        }

    }

    private fun changeActivity(userInfo: String) {
        //ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifeCycleListner())
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