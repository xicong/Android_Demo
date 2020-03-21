package com.cong.demo.shijianfenfa;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cong.demo.R;

public class ShijianfenfaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatch_layout);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("ttit", "DispatchActivity ://////dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("ttit", "DispatchActivity ://////onTouchEvent event ="+event.getAction());
        return true;
    }
}
