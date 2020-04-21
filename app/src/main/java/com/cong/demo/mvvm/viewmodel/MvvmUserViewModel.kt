package com.cong.demo.mvvm.viewmodel

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.cong.demo.BR
import com.cong.demo.mvvm.MvvmModelCallBack
import com.cong.demo.mvvm.model.MvvmModel

class MvvmUserViewModel : BaseObservable(){
    
    @Bindable
    var ageStr:String?= ""
        get() {
            return  field
        }
        set(value) {
            field = value
            notifyPropertyChanged(BR.ageStr)
        }
    
    private var mvvmModel:MvvmModel = MvvmModel()
    
    fun onClick(view:View){
        mvvmModel.addAge(object : MvvmModelCallBack{
            override fun onSuccess(num: Int) {
                ageStr = num.toString()
            }
            override fun onFailed(text: String) {
                ageStr=text
            }

        })
    }
    


}