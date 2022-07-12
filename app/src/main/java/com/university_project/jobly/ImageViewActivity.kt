package com.university_project.jobly

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.university_project.jobly.utils.DownloadFile

class ImageViewActivity : AppCompatActivity() {
    var imageUrl = "None"
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        imageUrl = intent.getStringExtra("imageUrl").toString()
        imageView = findViewById(R.id.iv_ViewImage_id)
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.image_loding_anim)
            .error(R.drawable.ic_profileimg)
            .into(imageView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.imageviw, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        DownloadFile.downloadFile(imageUrl, "image", this)
        return super.onOptionsItemSelected(item)
    }
}