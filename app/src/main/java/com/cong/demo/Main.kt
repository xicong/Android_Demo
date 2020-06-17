package com.cong.demo
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.cong.demo.activity.Activity_Xg
import com.cong.demo.base.BaseActivity
import com.cong.demo.bluetooth.getdata.BluetoothGetDataActivity
import com.cong.demo.fragment.Fragment_Xg
import com.cong.demo.jetpack.livedata.LIveDataActivity
import com.cong.demo.jetpack.navigation.NavigationDemo
import com.cong.demo.jetpack.room.RoomActivity
import com.cong.demo.shijianfenfa.ShijianfenfaActivity
import com.cong.demo.zidingyiview.ZdyXinLvTuActivity
import com.cong.demo.zidingyiview.Zdy_View_Zhexiantu_Activity
import kotlinx.android.synthetic.main.layout_main.*

class Main : BaseActivity(){
    
    var mList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        
//        initData()
//        initRecycleView()
        
        //开发模式  mvc  mvp  mvvm
//        ActivityUtils.startActivity(MvcActivity::class.java)
//        ActivityUtils.startActivity(MvpActivity::class.java)
//        ActivityUtils.startActivity(MvvmActivity::class.java)
        
        //事件分发
//        ActivityUtils.startActivity(ShijianfenfaActivity::class.java)
        
        //自定义view
//        ActivityUtils.startActivity(ZdyViewActivity::class.java)
//        ActivityUtils.startActivity(Zdy_view_textview_Activity::class.java)
//        ActivityUtils.startActivity(Zdy_view_jibuqi_Activity::class.java)
        ActivityUtils.startActivity(Zdy_View_Zhexiantu_Activity::class.java)
//        ActivityUtils.startActivity(ZdyXinLvTuActivity::class.java)  //自定义一个动态刷新的心率图

        
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
//        ActivityUtils.startActivity(RoomActivity::class.java)
        
        //蓝牙相关  用两个手机模拟手机与智能蓝牙硬件之间的数据互传
//        ActivityUtils.startActivity(BluetoothSendDataActivity::class.java)
//        ActivityUtils.startActivity(BluetoothGetDataActivity::class.java)  //扫描蓝牙并连接
        
        
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