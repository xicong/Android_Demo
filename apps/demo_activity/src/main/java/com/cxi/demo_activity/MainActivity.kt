package com.cxi.demo_activity

import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.cxi.demo_activity.ui.theme.Android_DemoTheme
import com.cxi.lib_base.utils.LogUtils

class MainActivity : ComponentActivity() {

    /**
     * 当我们点击activity的时候，系统会调用activity的oncreate()方法，在这个方法中我们会初始化当前布局setContentLayout（）方法。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.getInstance().i("=============onCreate=================")
        setContent {
            Android_DemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("1")
                }
            }
        }
    }

    /**
     * onCreate()方法完成后，此时activity进入onStart()方法,当前activity是用户可见状态，但没有焦点，与用户不能交互，一般可在当前方法做一些动画的初始化操作
     */
    override fun onStart() {
        super.onStart()
        LogUtils.getInstance().i("=============onStart=================")
    }

    /**
     * onStart()方法完成之后，此时activity进入onResume()方法中，当前activity状态属于运行状态 (Running)，可与用户进行交互。
     */
    override fun onResume() {
        super.onResume()
        LogUtils.getInstance().i("=============onResume=================")
    }

    /**
     * 当另外一个activity覆盖当前的acitivty时，此时当前activity会进入到onPouse()方法中，当前activity是可见的，但不能与用户交互状态。
     */
    override fun onPause() {
        super.onPause()
        LogUtils.getInstance().i("=============onPause=================")
    }


    /**
     * onPouse()方法完成之后，此时activity进入onStop()方法，此时activity对用户是不可见的，在系统内存紧张的情况下，有可能会被系统进行回收。所以一般在当前方法可做资源回收。
     */
    override fun onStop() {
        super.onStop()
        LogUtils.getInstance().i("=============onStop=================")
    }

    /**
     * onStop()方法完成之后，此时activity进入到onDestory()方法中，结束当前activity。
     */
    override fun onDestroy() {
        super.onDestroy()
        LogUtils.getInstance().i("=============onDestroy=================")
    }

    /**
     * onRestart()方法在用户按下home()之后，再次进入到当前activity的时候调用。调用顺序onPouse()->onStop()->onRestart()->onStart()->onResume().
     */
    override fun onRestart() {
        super.onRestart()
        LogUtils.getInstance().i("=============onRestart=================")
    }
    
}

@Composable
fun Greeting(name: String) {
    Box(
        modifier = Modifier.background(Color.Green)
    ){
        Text(
            text = "$name!",
            modifier = Modifier.clickable { 
                
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Android_DemoTheme {
        Greeting("1")
    }
}