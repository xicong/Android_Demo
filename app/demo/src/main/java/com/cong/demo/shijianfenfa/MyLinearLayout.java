package com.cong.demo.shijianfenfa;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MyLinearLayout extends LinearLayout {
    
    private String Tag = getClass().getName();

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 处理事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(Tag, "处理事件=============onTouchEvent"+event.getAction());
        return super.onTouchEvent(event);
    }

    /**
     * 拦截事件
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(Tag, "拦截事件=====================onInterceptTouchEvent");
        return true;
    }

    /**
     * 传递事件
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e(Tag, "分发事件=====================dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }
}
