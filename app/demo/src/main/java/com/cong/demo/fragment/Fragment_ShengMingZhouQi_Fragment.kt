package com.cong.demo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.cong.demo.R
import me.yokeyword.fragmentation.SupportFragment

class Fragment_ShengMingZhouQi_Fragment : SupportFragment(){

    /**
     * 在Fragment 和 Activity 建立关联是调用（Activity 传递到此方法内）
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtils.i("==============onAttach==================")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.i("==============onCreate==================")
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
    }

    /**
     * 当Fragment 创建视图时调用
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * ⚠️在fragment中参数3要设置为false，因为在fragment的内部实现中会把该布局添加到container，如果为true则会重复添加
         * 参数1，要获取的布局
         * 参数2，这个参数也是一个布局，是为第一个参数指定的父布局
         * 参数3, 如果第二个参数为空，这个参数将失去作用
         *           true 将第一个参数表示的布局添加到第二个参数的布局中
         *           false 不将第一个参数表示的布局添加到第二个参数的布局中
         *                     既然不添加为什么第二个参数不设置为null呐
         *                     不添加的花这个函数就只剩下一个作用了，那就是获取布局，为了使第一个参数的宽高不失效，所以要为他指定一个父布局
         */
        var view = inflater.inflate(R.layout.layout_fragment_shengmingzhouqi_fragment,container,false)
//        return super.onCreateView(inflater, container, savedInstanceState)
        LogUtils.i("==============onCreateView==================")
        return view
    }

    /**
     * 在相关联的 Activity 的 onCreate() 方法已返回时调用。
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LogUtils.i("==============onActivityCreated==================")
    }

    override fun onStart() {
        super.onStart()
        LogUtils.i("==============onStart==================")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.i("==============onResume==================")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.i("==============onPause==================")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.i("==============onStop==================")
    }

    /**
     * 当Fragment中的视图被移除时调用
     */
    override fun onDestroyView() {
        super.onDestroyView()
        LogUtils.i("==============onDestroyView==================")
    }

    /**
     * 
     */
    override fun onDestroy() {
        super.onDestroy()
        LogUtils.i("==============onDestroy==================")
    }

    /**
     * 当Fragment 和 Activity 取消关联时调用。
     */
    override fun onDetach() {
        super.onDetach()
        LogUtils.i("==============onDetach==================")
    }
    
}