package com.university_project.jobly.chatserver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.university_project.jobly.R
import com.university_project.jobly.utils.GetTheme

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        if (GetTheme.getDarkTheme(resources)) setTheme(R.style.Theme_SplashJoblyDarkNoActionBar)
        else setTheme(R.style.Theme_SplashJoblyLightNoActoinBar)
    }
}