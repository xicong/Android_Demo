package com.cxi.lib_base.utils

import android.text.SpannableStringBuilder
import android.util.Log

class LogUtils private constructor() {
    
    private val TAG = "XIXIANSEN"

    companion object {
        fun getInstance() = Helper.instance
    }

    private object Helper {
        val instance = LogUtils()
    }
    
    fun i(vararg content:Any){
        val msgSpan = SpannableStringBuilder()
        for (i in content.indices){
            msgSpan.append("          ")
            msgSpan.append(content[i].toString())
        }
        Log.i(TAG,msgSpan.toString())
    }
}
