package com.cong.demo
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.cong.demo.activity.Activity_Xg
import com.cong.demo.fragment.Fragment_Xg
import com.cong.demo.mergeadapter.MergeAdapterDemo
import com.cong.demo.shijianfenfa.ShijianfenfaActivity
import com.cong.demo.xiancheng.Activity_Kotlin_XieCheng
import com.cong.demo.zidingyiview.ZdyViewActivity
import kotlinx.android.synthetic.main.layout_main.*

class Main : BaseActivity(){
    
    var mList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        
//        initData()
//        initRecycleView()
        
        //事件分发
//        ActivityUtils.startActivity(ShijianfenfaActivity::class.java)
        
        //自定义view
//        ActivityUtils.startActivity(ZdyViewActivity::class.java)
//        ActivityUtils.startActivity(Zdy_view_textview_Activity::class.java)
//        ActivityUtils.startActivity(Zdy_view_jibuqi_Activity::class.java)

        
        //Acticity的生命周期
//        ActivityUtils.startActivity(Activity_Xg::class.java)
        
        //Fragment的生命周期
//        ActivityUtils.startActivity(Fragment_Xg::class.java)
        
        //线程相关
//        ActivityUtils.startActivity(Activity_Thread_Java::class.java)
//        ActivityUtils.startActivity(Activity_Kotlin_XieCheng::class.java)
        
        //MergeAdapter
        ActivityUtils.startActivity(MergeAdapterDemo::class.java)
    
    }
    
    fun initData(){
        mList.clear()
        mList.add("事件分发")
        mList.add("自定义view")
        mList.add("Activity")
        mList.add("Fragment")
    }
    
    fun initRecycleView(){
        rv_main.layoutManager = LinearLayoutManager(this)
//        rv_main.setHasFixedSize(true) 
        var adapter = RvAdapter(mList)
        rv_main.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->  
            when (position){
                0 -> {
                    ActivityUtils.startActivity(ShijianfenfaActivity::class.java)
                }
                1 -> {
                    ActivityUtils.startActivity(ZdyViewActivity::class.java)
                }
                2 -> {
                    ActivityUtils.startActivity(Activity_Xg::class.java)
                }
                3 -> {
                    ActivityUtils.startActivity(Fragment_Xg::class.java)
                }
            }
        }
    }
    
}