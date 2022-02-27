package com.university_project.jobly.utils.screensize

import kotlin.math.roundToInt

object GetAccountLogProperties {
    fun getFrameLayoutHeight(size:Float):Int{
        return (size*0.8).roundToInt()
    }
}