package com.cong.demo
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.cong.demo.shijianfenfa.ShijianfenfaActivity
import com.cong.demo.zidingyiview.ZdyViewActivity
import kotlinx.android.synthetic.main.main.*

class Main : BaseActivity(){
    
    var mList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        
        initData()
        initRecycleView()
    }
    
    fun initData(){
        mList.clear()
        mList.add("事件分发")
        mList.add("自定义view")
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
            }
        }
    }
    
}