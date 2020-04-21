package com.cong.demo.mvvm.view

import android.os.Bundle
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityMvvmBinding
import com.cong.demo.mvvm.viewmodel.MvvmUserViewModel

/**
 *https://www.jianshu.com/p/54f82b17c4d3
 * Model <------> ViewModel <------> View
 *
 *          Model层，主要负责数据的提供。Model层提供业务逻辑的数据结构（比如，实体类），
 *          提供数据的获取（比如，从本地数据库或者远程网络获取数据），提供数据的存储。
 *
 *          View层，主要负责界面的显示。View层不涉及任何的业务逻辑处理，它持有ViewModel层的引用，
 *          当需要进行业务逻辑处理时通知ViewModel层。
 *
 *          ViewModel层，主要负责业务逻辑的处理。ViewModel层不涉及任何的视图操作。
 *          通过官方提供的Data Binding库，View层和ViewModel层中的数据可以实现绑定，
 *          ViewModel层中数据的变化可以自动通知View层进行更新，
 *          因此ViewModel层不需要持有View层的引用。ViewModel层可以看作是View层的数据模型
 *          和Presenter层的结合。
 *          
 *作用
 *      降低View和控制模块的耦合，减轻了视图的压力
 *      
 *使用
 *      View与ViewModel进行绑定，能够实现双向的交互，ViewModel数据改变时，View会相应的变动UI，反之依然
 *      ViewModel进行业务逻辑处理，通知Model去更新
 *      Model数据更新后，把数据传递给ViewModel
 *
 * MVVM模式与MVP模式最大的区别在于：
 *      ViewModel与View绑定后，ViewModel与View其中一方的数据更新都能立即通知到对方，而mvp的
 *      p需要通过接口去通知view更新
 *      
 * MVVM的优点
 *      相比Mvp，p与v存在耦合  vm与v的耦合则更低 vm只负责处理和提供数据 ui的改变几乎不需要任何代码
 *      vm只包含数据和业务逻辑，没有ui的东西，方便单元测试
 * MVVM的缺点
 *      数据绑定的程序较难调试，界面出现异常时候有可能是view的代码有问题，也有可能是model的代码有问题
 */
class MvvmActivity : BaseActivity(){
    
    private lateinit var mBinding:LayoutActivityMvvmBinding
    private lateinit var mvvmViewModel:MvvmUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LayoutActivityMvvmBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mvvmViewModel = MvvmUserViewModel()
        mBinding.mvvmModel = mvvmViewModel
    }
    
}























