package com.university_project.jobly.utils.screensize

import kotlin.math.roundToInt

object RegisterFragmentSize {
   fun getRegisterNameWidth(size:Float):Int{
       return getIntVal(size*20)
   }
    fun getRegisterNameHeight(size:Float):Int{
        return getIntVal(size*25)
    }
    fun getRegFontSize(size: Float):Float{
        return size*10
    }
    fun getIntVal(value:Float):Int{
        return value.roundToInt()
    }
fun getRegEditHeght(size:Float):Int{
    return getIntVal(size*50)
}
}