package com.university_project.jobly.utils

import android.content.res.Configuration
import android.content.res.Resources

object GetTheme{
    fun getDarkTheme(resource: Resources):Boolean{
        val currentTheme=resource.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentTheme==Configuration.UI_MODE_NIGHT_YES)return true
        return false
    }
}