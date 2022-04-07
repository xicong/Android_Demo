package com.cong.demo.jetpack.navigation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cong.demo.R

class NvRvAdapter(data: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_item_rv, data) {
    
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_rv_item_main,item)
    }


}