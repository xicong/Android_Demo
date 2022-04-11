package com.cong.demo.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

/**
 * AndroidViewModel就是一个单独的class 将数据保存起来，不会随生命周期一样被销毁
 */
class CxiViewModel(application:Application)  : AndroidViewModel(application){
    
    var count = 0;
    
}