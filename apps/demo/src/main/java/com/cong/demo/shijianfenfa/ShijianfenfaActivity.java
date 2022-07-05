package com.cong.demo.shijianfenfa;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import androidx.annotation.Nullable;
import com.cong.demo.R;
import com.cong.demo.base.BaseActivity;

public class ShijianfenfaActivity extends BaseActivity {
    
    private String Tag = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shijianfenfa_layout);
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

    /**
     * 处理事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(Tag, "onTouchEvent event============="+event.getAction());
        return true;
    }
}
