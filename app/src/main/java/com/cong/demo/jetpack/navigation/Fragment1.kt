package com.cong.demo.jetpack.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cong.demo.R
import com.cong.demo.base.BaseFragment
import com.cong.demo.databinding.LayoutFragmentOBinding
import kotlinx.android.synthetic.main.layout_main.*

class Fragment1 : BaseFragment() {

    private lateinit var binding:LayoutFragmentOBinding


    var mList = arrayListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutFragmentOBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener { 
            val controller = it.findNavController()
            //不推荐 直接 用fragment  推荐用action  可以方便设置activity动画之类的 在视图上也能方便的看到跳转路径
//            controller.navigate(R.id.fragment2)
            
            //传参  
//            val bundle = Bundle()
//            bundle.putString("name","xiaowang")
//            controller.navigate(R.id.action_fragment1_to_fragment2,bundle)

            // 推荐在nav_group里面设置
            //加插件自动生成参数常量  
//            classpath 'android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0'
//            apply plugin: 'androidx.navigation.safeargs'
//            implementation 'androidx.navigation:navigation-safe-args-gradle-plugin:2.3.0-alpha04'
            controller.navigate(Fragment1Directions.actionFragment1ToFragment2("你好"))
            
            

            
        }

        mList.clear()
        for (i in 0..100){
            mList.add("$i")
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        val ad = NvRvAdapter(mList)
        binding.recyclerview.adapter = ad
    }

}
