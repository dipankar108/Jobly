package com.university_project.jobly.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object ResetPassword {

    fun changePassword(context: Context) {
        val muser = Firebase.auth.currentUser
        muser?.let { user ->
            Firebase.auth.sendPasswordResetEmail(user.email.toString())
                .addOnCompleteListener {
                    Log.d("TAG", "onOptionsItemSelected: Link send working")
                    if (it.isSuccessful) {
                        Log.d("TAG", "onOptionsItemSelected: Link send")
                        Toast.makeText(
                            context,
                            "Password change link send to your email",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.d("TAG", "onOptionsItemSelected: Link send failed")
                        Toast.makeText(
                            context,
                            it.exception?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }

    }
}