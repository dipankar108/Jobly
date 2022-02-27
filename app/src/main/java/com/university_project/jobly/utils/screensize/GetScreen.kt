package com.university_project.jobly.utils.screensize

import android.content.res.Resources

class GetScreen(val display: Resources) {
    fun getWidth(): Float {
        return display.displayMetrics.widthPixels/display.displayMetrics.density
    }
    fun getHeight(): Float {
        return display.displayMetrics.heightPixels/display.displayMetrics.density
    }
    fun getGeneralDp():Float{
        return getHeight()/getWidth()
    }    }