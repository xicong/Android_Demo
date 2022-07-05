package com.cong.demo.mvp

/**
 * 暴露给presenter的方法
 */
interface IView {
    
    fun  updateUI(text:String)
    
}