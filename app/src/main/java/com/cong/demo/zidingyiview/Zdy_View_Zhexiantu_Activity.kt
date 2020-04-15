package com.cong.demo.zidingyiview

import android.os.Bundle
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityZdyViewZhexiantuBinding
import com.cong.demo.zidingyiview.data.ZhexiantuData

class Zdy_View_Zhexiantu_Activity : BaseActivity(){
    
    var mList = arrayListOf<ZhexiantuData>()

    private lateinit var binding:LayoutActivityZdyViewZhexiantuBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityZdyViewZhexiantuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        
        binding.customView1.setData(mList)
    }

    private fun initData() {
        mList.add(ZhexiantuData("12.02",1f))
        mList.add(ZhexiantuData("12.10",12f))
        mList.add(ZhexiantuData("12.01",14f))
        mList.add(ZhexiantuData("12.16",3f))
        mList.add(ZhexiantuData("12.17",1f))
        mList.add(ZhexiantuData("12.15",5f))
        mList.add(ZhexiantuData("12.03",1f))
        mList.add(ZhexiantuData("12.16",8f))
        mList.add(ZhexiantuData("12.14",9f))
        mList.add(ZhexiantuData("12.17",11f))
        mList.add(ZhexiantuData("12.13",4f))
        mList.add(ZhexiantuData("12.12",3f))
        mList.add(ZhexiantuData("12.16",1f))
        mList.add(ZhexiantuData("12.03",7f))
        mList.add(ZhexiantuData("12.05",1f))
        mList.add(ZhexiantuData("12.08",12f))
        mList.add(ZhexiantuData("12.15",1f))
        mList.add(ZhexiantuData("12.14",18f))
        mList.add(ZhexiantuData("12.17",19f))
        mList.add(ZhexiantuData("18",50f))
    }
}