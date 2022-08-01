package com.university_project.jobly.verify

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.university_project.jobly.R
import com.university_project.jobly.client.ClientActivity
import com.university_project.jobly.databinding.ActivityVerifyBinding
import com.university_project.jobly.employee.EmployeeActivity

class VerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyBinding
    private var imageUri = Uri.EMPTY
    private var name: String = "none"
    private var dob: String = "none"
    private var userType: String = "none"
    private var id: String = "none"
    private var docID: String = "none"
    private lateinit var modelVerify: ModelVerify
    private val dbVer = Firebase.firestore.collection("VerificationNID")
    private val dbProfile = Firebase.firestore.collection("User")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userType = intent.getStringExtra("userType").toString()
        var uploadImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { imageRes: ActivityResult ->
                if (imageRes.resultCode == Activity.RESULT_OK) {
                    imageUri = Uri.parse(imageRes.data?.data.toString())
                    binding.ivNIDVIEWId.setImageURI(imageUri)
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    val image = InputImage.fromBitmap(bitmap, 0)
                    val recognizer: TextRecognizer = TextRecognition.getClient()
                    val dialog = Dialog(this)
                    dialog.setContentView(R.layout.progressbarlayout)
                    dialog.setCancelable(false)
                    dialog.show()
                    recognizer.process(image)
                        .addOnSuccessListener { texts ->
                            //   mTextButton.setEnabled(true)
                            processTextRecognitionResult(texts as Text)

                            dialog.dismiss()
                        }
                        .addOnFailureListener(
                            OnFailureListener { e -> // Task failed with an exception
                                // mTextButton.setEnabled(true)
                                e.printStackTrace()
                                dialog.dismiss()
                            })
                }
            }
        binding.btnUploadNIDId.setOnClickListener {
            val intent = Intent()
            intent.type = ("image/*")
            intent.action = Intent.ACTION_GET_CONTENT
            uploadImage.launch(intent)

        }
        binding.btnUploadNIDVerifyId.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.progressbarlayout)
            dialog.setCancelable(false)
            dialog.show()
            if (!modelVerify.alreadyExists) {
                if (name == modelVerify.name && id == modelVerify.id && dob == modelVerify.dob) {
                    dbProfile.document(Firebase.auth.uid.toString())
                        .update("verify", true).addOnSuccessListener {

                            dbVer.document(docID)
                                .update("registerId", Firebase.auth.uid.toString())
                                .addOnSuccessListener {
                                    dbVer.document(docID)
                                        .update("alreadyExists", true)
                                        .addOnSuccessListener {
                                            dialog.dismiss()
                                            if (userType == "Client") {
                                                startActivity(
                                                    Intent(
                                                        this,
                                                        ClientActivity::class.java
                                                    ).apply {
                                                        finish()
                                                    })
                                            } else {
                                                startActivity(
                                                    Intent(
                                                        this,
                                                        EmployeeActivity::class.java
                                                    ).apply {
                                                        finish()
                                                    })
                                            }
                                            Toast.makeText(
                                                this,
                                                "Success",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()
                                        }
                                }


                        }.addOnFailureListener {
                            dialog.dismiss()
                            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                        }
                } else {
                    dialog.dismiss()
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
            } else {
                dialog.dismiss()
            }
        }

    }

    private fun checkVerificationStatus() {
        if (name != "none" && dob != "none" && id != "none") {
            dbVer.whereEqualTo("id", id).addSnapshotListener { value, error ->
                value?.let {
                    for (doc in it.documentChanges) {
                        modelVerify = doc.document.toObject(ModelVerify::class.java)
                        docID = doc.document.id
                    }
                    if (modelVerify.alreadyExists) {
                        binding.tvNIDAlreadyRegisteredId.text = "NID already Registered"
                        binding.tvNIDAlreadyRegisteredId.visibility = View.VISIBLE
                    } else {
                        binding.tvNIDAlreadyRegisteredId.text = "Ready to verify"
                        binding.tvNIDAlreadyRegisteredId.visibility = View.VISIBLE
                    }
                }

            }
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun processTextRecognitionResult(texts: Text) {
        val blocks: List<Text.TextBlock> = texts.textBlocks
        if (blocks.isEmpty()) {
            Toast.makeText(this, "No text found", Toast.LENGTH_LONG).show()
            return
        }
        for (i in blocks) {
            var textm = i.text.trim()
            if (textm.contains("ID NO")) {
                id = textm.replace("ID NO: ", "")
                Log.d("VERIFICATION", "processTextRecognitionResult: $id")
            }
            if (textm.contains("Date of Birth:")) {
                dob = textm.replace("Date of Birth: ", "")
            }
            if (textm.contains("Name:")) {
                name = textm.replace("Name: ", "")
            }
        }
        checkVerificationStatus()
    }
}