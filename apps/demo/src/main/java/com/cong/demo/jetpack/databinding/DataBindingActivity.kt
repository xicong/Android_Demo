package com.cong.demo.jetpack.databinding

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils.concat
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityDatabindingDemoBinding

/**
 * 单项绑定  Activity中数据自动显示在界面上
 * 
 * 双项绑定  mvvm的精髓  view改变了数据也会跟着改变
 */
class DataBindingActivity : BaseActivity() {
    
    private lateinit var binding:LayoutActivityDatabindingDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityDatabindingDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
//        val user = User("小网",20)
        val user = User()
        user.name ="https"
        user.age = 100
        user.avator = "https://timgsa.baidu.com/timg?image&quality=80&size=b" +
                "9999_10000&sec=1586860885337&di=ca89a30f3755dd7461912a28" +
                "312d18f6&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%" +
                "2Fimages%2Fupload%2Fupc%2Ftx%2Fitbbs%2F2003%2F05%2Fc4%" +
                "2F195593837_1583372967744_1024x1024it.jpg"
        binding.userBean = user

        //单项绑定
//        binding.btnActivityDatabingDemoUpdate.setOnClickListener {
//            user.name.set("小网")
//            user.age.set(100)
//        }
//        binding.btnActivityDatabingDemoUpdate.setOnClickListener {
//            user.name = "小网"
//            user.age = 100
//        }
        
        //双项绑定
        //在布局里面直接用edittex给name赋值
        //事件绑定
        binding.listeren = BtnListener(user)
        
        //自定义属性
        
    }
    
    @BindingAdapter("stringPinjie")
    fun stringPinjie(tv:TextView,str1:String,str2:String):String{
        return str1+str2
    }
    
    class BtnListener(val user:User){
        fun chengeAge(){
            user.age = user.age+1
        }
        fun changeName(s:Editable){
            user.name = s.toString()
        }
    }
    
}