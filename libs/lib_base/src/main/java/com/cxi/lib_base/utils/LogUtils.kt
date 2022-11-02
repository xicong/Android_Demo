package com.cxi.lib_base.utils

class LogUtils private constructor() {

    companion object {
        fun getInstance() = Helper.instance
    }

    private object Helper {
        val instance = LogUtils()
    }
    
    fun i(vararg content:Any){
        com.blankj.utilcode.util.LogUtils.i(content)
    }
}
