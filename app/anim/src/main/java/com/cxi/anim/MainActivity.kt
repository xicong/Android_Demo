package com.cxi.anim

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ObjectAnimator.ofFloat(textview,"rotation",0f,360f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = -1  //-1表示无限循环
            start()
        }
        
    }
}