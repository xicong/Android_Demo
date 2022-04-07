package com.cong.demo.jetpack.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class ImageAdapter {
    
    companion object{

        /**
         * value 显示的属性名字
         * 
         * requireAll 参数必须全部都有
         */
        @JvmStatic
        @BindingAdapter(value = ["android:url"],requireAll = true)
        fun loadImage(imageview:ImageView,url:String?){
            if (url.isNullOrEmpty()){
                return
            }
            Glide.with(imageview)
                .load(url)
                .into(imageview)
        }
    }

    
}