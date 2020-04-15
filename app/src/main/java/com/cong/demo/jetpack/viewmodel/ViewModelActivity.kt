package com.cong.demo.jetpack.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cong.demo.app.CxiApp
import com.cong.demo.base.BaseActivity
//import com.cong.demo.databinding.LayoutActivityViewmodelBinding

/**
 * ViewModel 为Activity瘦身
 * 保存管理数据
 */
class ViewModelActivity : BaseActivity(){

//    private lateinit var binding:LayoutActivityViewmodelBinding
    private lateinit var mViewModel:CxiViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = LayoutActivityViewmodelBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(CxiApp.instance))
            .get(CxiViewModel::class.java)
//        binding.tvActivityViewmodelNum.text = "${mViewModel.count}"
//        binding.tvActivityViewmodelAdd1.setOnClickListener {
//            mViewModel.count++
//            binding.tvActivityViewmodelNum.text = "${mViewModel.count}"
//        }
//        binding.tvActivityViewmodelAdd2.setOnClickListener {
//            mViewModel.count+=2
//            binding.tvActivityViewmodelNum.text = "${mViewModel.count}"
//        }
    }
    
}