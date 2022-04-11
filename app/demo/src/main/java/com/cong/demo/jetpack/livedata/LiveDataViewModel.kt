package com.cong.demo.jetpack.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Delayed

class LiveDataViewModel : ViewModel(){
    
    val livedata = MutableLiveData<Int>()
    var isStop = false
    
    fun start(){
        livedata.postValue(0)
        GlobalScope.launch {
            while (!isStop){
                delay(1000)
                var  value = livedata.value
                if(value!=null){
                    livedata.postValue(value+1)
                }
            }
        }
    }
    
    fun stop(){
        isStop = true
    }
    
}