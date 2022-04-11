package com.cong.demo.mvc.controllerandview

import android.os.Bundle
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityMvcBinding
import com.cong.demo.mvc.model.MvcModel

/**
 * 
 * https://www.jianshu.com/p/784bcb8f2da4
 * 
 * 
 * 该例子中的角色Controller  View  一方面接收来自view的事件，一方面通知model处理数据  
 *  
 *                               
 *          Model -------------------> View --------------> Controller( -----------> Model)
 * 
 *          Model：数据操作 网络操作 业务计算操作  
 *          View  xml文件用来展示view  
 *          Controller  Activity  activity主线程响应时间只有5s 耗时的操作放在这里容易被回收
 *          
 *          View 传送指令到 Controller
 *          Controller 完成业务逻辑后，要求 Model 改变状态
 *          Model 将新的数据发送到 View，用户得到反馈
 *          
 *          优点：视图层（view） 与 模型层（model）解耦  通过controll来进行联系
 *                  模块职责划分明确  主要划分mvc三个模块，利于代码维护
 *          
 *          缺点：Activity充当Controller加载布局初始化界面并接受处理来自用户的操作请求进而做出响应
 *                  随着界面及其逻辑的复杂度不断提升，Activity会变的特别臃肿，例如进度条ui其实还是activity控制的
 *                 view 和 model还有交互，没有完全分离
 *                  
 *         Android中的经典例子 ListView         
 */
class MvcActivity : BaseActivity(){
    
    private lateinit var mBinding:LayoutActivityMvcBinding
    private var mvcModel:MvcModel? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LayoutActivityMvcBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        
        mvcModel = MvcModel()
        
        mBinding.btnActivityMvcAgeAdd.setOnClickListener { 
          updataUi(mvcModel!!.addAge().toString())
        }
        
    }
    
    fun updataUi(text:String){
        mBinding.tvActivityMvcAge.text = text
    }
    
}