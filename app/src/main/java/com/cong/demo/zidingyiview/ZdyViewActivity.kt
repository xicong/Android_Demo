package com.cong.demo.zidingyiview

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.cong.demo.BaseActivity
import com.cong.demo.RvAdapter
import com.cong.demo.R
import kotlinx.android.synthetic.main.main.*

class ZdyViewActivity : BaseActivity(){

    var mList = arrayListOf<String>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        initData()
        initRecycleView()
    }

    fun initData(){
        mList.clear()
        mList.add("自定义Text")
        mList.add("自定义记步圈")
    }

    fun initRecycleView(){
        rv_main.layoutManager = LinearLayoutManager(this)
//        rv_main.setHasFixedSize(true) 
        var adapter = RvAdapter(mList)
        rv_main.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            when (position){
                0 -> {
                    ActivityUtils.startActivity(Zdy_view_textview_Activity::class.java)
                }
                1 -> {
                    ActivityUtils.startActivity(Zdy_view_jibuqi_Activity::class.java)
                }
            }
        }
    }
    
}