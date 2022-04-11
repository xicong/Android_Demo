package com.cong.demo.zidingyiview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.cong.demo.R
import kotlinx.android.synthetic.main.zdyv_view_jibuqi.*

class Zdy_view_jibuqi_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zdyv_view_jibuqi)
        
        val animator=ObjectAnimator.ofFloat(0f,300f)
        animator.interpolator=DecelerateInterpolator()
        animator.duration=1000000
        animator.addUpdateListener(object:ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                jbq.setStepText(animation!!.animatedValue as Float)
            }
        })
        animator.start()
    }
}