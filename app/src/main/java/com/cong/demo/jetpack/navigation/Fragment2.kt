package com.cong.demo.jetpack.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.blankj.utilcode.util.LogUtils
import com.cong.demo.R
import com.cong.demo.base.BaseFragment
import com.cong.demo.databinding.LayoutFragmentTBinding


class Fragment2 : BaseFragment() {
    
    private lateinit var binding:LayoutFragmentTBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=LayoutFragmentTBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //获取到传过来到参数
//        LogUtils.i(arguments?.getString("name"))
        
        val args = arguments?.let { Fragment2Args.fromBundle(it)}
        LogUtils.i(args!!.name)
        
        binding.button.setOnClickListener {
            //navigate依次会压栈 每次都会在栈里面增加
//            it.findNavController().navigate(R.id.action_fragment2_to_fragment1)
            //这个是弹出栈的操作  也就是说移除栈的操作   和返回键逻辑一致
            it.findNavController().popBackStack()
        }

        binding.button1.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragment2_to_mobile_navigation)
        }

        binding.button2.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragment2_to_bottomNavigationActivity)
        }
    }

}
