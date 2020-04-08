package com.cong.demo

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvAdapter(data: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item, data) {
    
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_rv_item_main,item)
    }


}