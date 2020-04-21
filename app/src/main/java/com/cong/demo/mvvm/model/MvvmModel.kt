package com.cong.demo.mvvm.model

import com.cong.demo.mvvm.MvvmModelCallBack

class MvvmModel {
    
    var num = 0;
    
    fun  addAge(mvvmModelCallBack: MvvmModelCallBack){
        mvvmModelCallBack.onSuccess(++num)
    }
    
}