package com.cong.demo.mvp.view

import android.os.Bundle
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityMvpBinding
import com.cong.demo.mvp.IView
import com.cong.demo.mvp.presenter.MvpUserPresenter

/**
 * https://www.jianshu.com/p/a8b03ba2b146
 * 该类在mvp中充当view 同时持有Presenter的饮用后
 * 
 * Model  <------   Presenter <------> View
 * 
 *      view接受用户的请求，然后传递给Presenter
 *      Presenter进行业务逻辑处理，修改Model
 *      Presenter通知view更新
 * 
 * MVP模式将应用分为三层：
 * 
 *        Model层主要负责数据的加载，存储 ，计算
 *
 *        View层主要负责界面的显示，
 *
 *        Presenter层主要负责业务逻辑的处理。
 *
 *
 * 在MVP模式中，Model层和View层不能直接通信，Presenter层负责充当中间人，
 * 实现Model层和View层之间的间接通信。View层和Presenter层互相持有对方的引用，
 * 实现View层和Presenter层之间的通信。
 *
 * MVP模式的主要优点是：
 *      分离了Model层和View层，分离了视图操作和业务逻辑，降低了耦合。修改视图不影响模型
 *      可以更高效的使用模型，因为所有的交互都发生在Presebter中
 *      Presebter与view的交互是通过接口进行而来的，有利于添加单元测试
 *      解决mvc中Activity职责过多，代码臃肿
 *      
 *Mvp的缺点
 *      页面逻辑复杂的话，相应的接口也会变多，增加维护成本，可以定义一些基本的类去分离公共的逻辑。
 *      系统内存不足时，系统会回收Activity，一般都在OnSaveInstenceState去保存状态，用OnRe
 *      storeInstanceState去恢复保存，但是在Mvp中view层不应该直接操作Model的，所以这样做
 *      不合理，同时也增加来Modle和View的耦合。
 *      Ui改变可能导致Presebter的一些更新Ui接口也跟着更改，存在一定的耦合
 *      
 * Mvc和Mvp的区别
 *          Mvp的view和model并不直接交互
 * 
 */
class MvpActivity : BaseActivity(),IView{
    
    private lateinit var mBinding:LayoutActivityMvpBinding
    private lateinit var mPresenter: MvpUserPresenter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LayoutActivityMvpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mPresenter = MvpUserPresenter(this)

        mBinding.btnActivityMvpAgeAdd.setOnClickListener { 
            mPresenter.add()
        }
    }

    override fun updateUI(text: String) {
        mBinding.tvActivityMvpAge.text = text
    }


}