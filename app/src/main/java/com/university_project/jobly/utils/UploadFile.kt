package com.university_project.jobly.utils

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class UploadFile() {
    fun getFile(
        intent: Intent,
        lifecycleOwner: LifecycleOwner,
        resultRegistry: ActivityResultRegistry
    ): Uri {
        intent.type = ("image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        var imageUri = Uri.EMPTY
        var uploadImage =
            resultRegistry.register("key", lifecycleOwner, ActivityResultContracts.GetContent()) {
                imageUri = it
            }
        return imageUri
    }
}