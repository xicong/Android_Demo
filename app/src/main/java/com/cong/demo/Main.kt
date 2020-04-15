package com.cong.demo
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.cong.demo.activity.Activity_Xg
import com.cong.demo.base.BaseActivity
import com.cong.demo.fragment.Fragment_Xg
import com.cong.demo.jetpack.LifeCycleViewModelLIveDataDataBindingDemo
import com.cong.demo.jetpack.databinding.DataBindingActivity
import com.cong.demo.jetpack.lifecycle.LifecycleActivity
import com.cong.demo.jetpack.livedata.LIveDataActivity
import com.cong.demo.jetpack.navigation.NavigationDemo
import com.cong.demo.jetpack.room.RoomActivity
import com.cong.demo.jetpack.viewmodel.ViewModelActivity
import com.cong.demo.shijianfenfa.ShijianfenfaActivity
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
//        ActivityUtils.startActivity(Zdy_View_Zhexiantu_Activity::class.java)

        
        //Acticity的生命周期
//        ActivityUtils.startActivity(Activity_Xg::class.java)
        
        //Fragment的生命周期
//        ActivityUtils.startActivity(Fragment_Xg::class.java)
        
        //线程相关
//        ActivityUtils.startActivity(Activity_Thread_Java::class.java)
//        ActivityUtils.startActivity(Activity_Kotlin_XieCheng::class.java)
        
        //MergeAdapter
//        ActivityUtils.startActivity(MergeAdapterDemo::class.java)
    
        //jetpack
//        ActivityUtils.startActivity(LifecycleActivity::class.java)
//        ActivityUtils.startActivity(ViewModelActivity::class.java)
//        ActivityUtils.startActivity(LIveDataActivity::class.java)
//        ActivityUtils.startActivity(DataBindingActivity::class.java)
//        ActivityUtils.startActivity(LifeCycleViewModelLIveDataDataBindingDemo::class.java)
//        ActivityUtils.startActivity(NavigationDemo::class.java)
        ActivityUtils.startActivity(RoomActivity::class.java)
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