package com.cong.demo.mvp.model

import com.cong.demo.mvp.MvpModelCallBack

/**
 * 跟mvc不同的地方在于Model不会跟view发生交互，只会跟Presenter交互
 */
class MvpModel {
    
    private var age:Int = 0
    
    fun ageAdd(modelCallBack: MvpModelCallBack){
        modelCallBack.onSuccess(++age)
    }
    
}