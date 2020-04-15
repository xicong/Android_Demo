package com.cong.demo.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.cong.demo.base.BaseActivity
import com.cong.demo.R
import com.cong.demo.RvAdapter
import kotlinx.android.synthetic.main.layout_main.*

class Activity_Xg  : BaseActivity(){

    var mList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        
        initData()
        initRecycleView()
        
    }

    fun initData(){
        mList.clear()
        mList.add("Activity的生命周期")
    }

    fun initRecycleView(){
        rv_main.layoutManager = LinearLayoutManager(this)
//        rv_main.setHasFixedSize(true) 
        var adapter = RvAdapter(mList)
        rv_main.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            when (position){
                0 -> {
                    ActivityUtils.startActivity(Activity_ShengMingZhouQi::class.java)
                }
            }
        }
    }
    
}