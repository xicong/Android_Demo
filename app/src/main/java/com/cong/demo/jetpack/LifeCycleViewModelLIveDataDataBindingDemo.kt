package com.cong.demo.jetpack

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityLVLDDemoBinding

class LifeCycleViewModelLIveDataDataBindingDemo : BaseActivity(){

    private lateinit var binding:LayoutActivityLVLDDemoBinding
    private lateinit var viewModel:UserViewModel
    private lateinit var user: User
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityLVLDDemoBinding.inflate(layoutInflater)
        
        setContentView(binding.root)
        viewModel=ViewModelProvider(this).get(UserViewModel::class.java)
        
        user = User()
        viewModel.liveDateUser.postValue(user)
        binding.userViewmodel = viewModel
        binding.lifecycleOwner = this
    }
}