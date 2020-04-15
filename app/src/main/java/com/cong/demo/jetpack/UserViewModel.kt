package com.cong.demo.jetpack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel(){
    
    val  liveDateUser = MutableLiveData<User>()
    
    fun agePlus(){
        var value = liveDateUser.value
        if(value == null){
            val user = User()
            user.age = 0
            user.name = "name"
            user.headimg = ""
        }
        value?.age = value?.age?.plus(1)!!
        value.name = "王武"
        value.headimg = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1586860885337&di=ca89a30f3755dd7461912a28312d18f6&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fitbbs%2F2003%2F05%2Fc4%2F195593837_1583372967744_1024x1024it.jpg"
        liveDateUser.postValue(value)
    }
    
}