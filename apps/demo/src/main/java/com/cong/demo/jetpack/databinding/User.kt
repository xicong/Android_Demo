package com.cong.demo.jetpack.databinding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import com.cong.demo.BR

/**
 * class 前面加data就是默认实现了set get
 */
//data class User(var name:String,var age:Int){
//    var mName : String = name
//        get() {
//            return  "用户名: "+name
//        }
//    var mAge : String = age.toString()
//        get() {
//            return  "年龄: "+age
//        }
//}

/**
 * 单项绑定
 * 
 * 这里需要手动实现  所以class前面不要data
 */
class User : BaseObservable(){
    
    @Bindable
    var age: Int = 0
    set(value) {
        field = value
        notifyPropertyChanged(BR.age)  //只更新单个数据
//        notifyChange()  //更新所有的
    }

    @Bindable
    var name: String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.name)
    }
    
    @Bindable
    var avator:String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.avator)
    }
    
    
    //简单实现同样可以更新所有数据
//    var age:ObservableField<Int> = ObservableField(0)
//    var name:ObservableField<String> = ObservableField("0")
    
}