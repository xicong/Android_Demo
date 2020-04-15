package com.cong.demo.mergeadapter

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cong.demo.base.BaseActivity
import com.cong.demo.databinding.LayoutActivityMergeadapterBinding
import com.cong.demo.mergeadapter.adapter.FootAdapter
import com.cong.demo.mergeadapter.adapter.StudentAdapter
import com.cong.demo.mergeadapter.adapter.TeacherAdapter

/**
 * 在最新的 recyclerview:1.2.0-alpha02[1] 中发布了一个关于 Adapter 的新特性 MergeAdapter 。我们可以 “合并”Adapter，或者说给 Adapter “做加法”。
 */
class MergeAdapterDemo : BaseActivity(){

    private lateinit var mViewModel: MainViewModel
    private val teacherAdapter by lazy { TeacherAdapter() }
    private val studentAdapter by lazy { StudentAdapter() }
    private val footAdapter by lazy { FootAdapter() }
    private lateinit var binding: LayoutActivityMergeadapterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityMergeadapterBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setTopViewMarginStatusHeight(binding.root)
        subTopViewMarginStatusHeight(binding.root)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initRecyclerView()
        observe()
        initData()
    }
    
    private fun initData() {
        mViewModel.loadStudent()
    }

    private fun initRecyclerView() {
        binding.rvActivityMergeadapter.run {this@MergeAdapterDemo
            val manager = LinearLayoutManager(this@MergeAdapterDemo)
            layoutManager = manager
            adapter = MergeAdapter(teacherAdapter, studentAdapter, footAdapter)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 && manager.findLastVisibleItemPosition() == manager.itemCount - 1) {
                        mViewModel.loadStudent()
                    }
                }
            })
        }
    }

    private fun observe() {
        mViewModel.run {
            teacherData.observe(this@MergeAdapterDemo, Observer { teacherAdapter.submitList(it) })
            studentData.observe(this@MergeAdapterDemo, Observer { studentAdapter.submitList(it) })
            loadState.observe(this@MergeAdapterDemo, Observer {
                footAdapter.submitList(arrayListOf(it))
            })
        }
    }
    
}