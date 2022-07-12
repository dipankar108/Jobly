package com.university_project.jobly.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

object DownloadFile {
    fun downloadFile(imageUrl: String, title: String, context: Context) {
        val req = DownloadManager.Request(Uri.parse(imageUrl))
            .setDescription(Environment.DIRECTORY_DOWNLOADS)
            .setTitle(title)
            .setDescription("Downloading")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(req)
    }
}