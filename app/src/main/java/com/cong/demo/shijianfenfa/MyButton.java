package com.cong.demo.shijianfenfa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class MyButton extends Button {
    
    private String Tag = getClass().getName();

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 处理事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(Tag, "onTouchEvent event============="+event.getAction());
        return super.onTouchEvent(event);
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
