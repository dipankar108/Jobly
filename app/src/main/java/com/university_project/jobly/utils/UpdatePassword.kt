package com.university_project.jobly.utils

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university_project.jobly.R

class UpdatePassword(private val context: Context, layoutInflater: LayoutInflater) {
    val dialog = Dialog(context)
    val pdialog = Dialog(context)
    val view = layoutInflater.inflate(R.layout.changepassword, null, false)
    val auth = Firebase.auth
    fun showDialog() {
        Log.d("Update", "Update Password:called ")
        dialog.setContentView(view)
        dialog.show()
        val oldPass: EditText = view.findViewById(R.id.et_OldPass_Update_id)
        val newPass: EditText = view.findViewById(R.id.et_newPassword_Update_id)
        val conPass: EditText = view.findViewById(R.id.et_conPassword_Update_id)
        val submit: Button = view.findViewById(R.id.btn_submitPass_update_id)
        submit.setOnClickListener {
            val myOldPassword = oldPass.text.toString()
            val myNewPassword = newPass.text.toString()
            val myConfirmPassword = conPass.text.toString()
            if (checkPassword(myNewPassword, myConfirmPassword, myOldPassword)) {
                pdialog.setContentView(R.layout.progressbarlayout)
                pdialog.show()
                val user = auth.currentUser
                user?.email?.let { userEmail ->
                    val credential = EmailAuthProvider.getCredential(userEmail, myOldPassword)
                    user.reauthenticate(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user.updatePassword(myNewPassword).addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    showToastMessage("Password has been successfully updated")
                                    dialog.dismiss()
                                    pdialog.dismiss()
                                    oldPass.text.clear()
                                    newPass.text.clear()
                                    conPass.text.clear()
                                } else {
                                    pdialog.dismiss()
                                    showToastMessage(updateTask.exception?.message.toString())
                                }
                            }
                        } else {
                            pdialog.dismiss()
                            showToastMessage(task.exception?.message.toString())
                        }
                    }
                }

            }
        }
    }

    fun checkPassword(
        myNewPassword: String,
        myConfirmPassword: String,
        myOldPassword: String
    ): Boolean {
        return when {
            myOldPassword.isEmpty() -> {
                showToastMessage("Old Password field cannot be empty")
                false
            }
            myNewPassword.isEmpty() -> {
                showToastMessage("New Password field cannot be empty")
                false
            }
            myConfirmPassword.isEmpty() -> {
                showToastMessage("Confirm password field cannot be empty")
                false
            }
            myNewPassword != myConfirmPassword -> {
                showToastMessage("New password and confirm password not matched")
                false
            }
            else -> true
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}