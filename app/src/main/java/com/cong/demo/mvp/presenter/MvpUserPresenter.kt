package com.cong.demo.mvp.presenter

import com.cong.demo.mvp.IPresenter
import com.cong.demo.mvp.IView
import com.cong.demo.mvp.MvpModelCallBack
import com.cong.demo.mvp.model.MvpModel

/**
 * 具体的业务逻辑
 */
class MvpUserPresenter(private val view:IView) : IPresenter,MvpModelCallBack{
    
    private var mMvpModel:MvpModel= MvpModel()
    
    override fun onSuccess(num: Int) {
        view.updateUI(num.toString())
    }

    override fun onFailed(text: String) {
        view.updateUI("失败")
    }

    override fun add() {
        mMvpModel.ageAdd(this)
    }
}