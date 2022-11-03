package com.cxi.demo_activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cxi.demo_activity.ui.theme.Android_DemoTheme
import com.cxi.lib_base.ext.getActivity
import com.cxi.lib_base.ext.registerBroadcast
import com.cxi.lib_base.utils.BroadcastUtils
import com.cxi.lib_base.utils.LogUtils

class MainActivity : ComponentActivity() {

    /**
     * 当我们点击activity的时候，系统会调用activity的oncreate()方法，在这个方法中我们会初始化当前布局setContentLayout（）方法。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.getInstance().i("=============onCreate=================")
        registerBroadcast(object:BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                LogUtils.getInstance().i("解锁了")
            }
        },Intent.ACTION_USER_PRESENT)
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

    /**
    1、当用户按下HOME键时，屏幕被关闭时。
    2、从当前activity启动一个新的activity时。
    3、屏幕方向切换时。
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        LogUtils.getInstance().i("=============onSaveInstanceState=================")
    }

    /**
    onRestoreInstanceState(Bundle savedInstanceState)只有在activity确实是被系统回收，重新创建activity的情况下才会被调用，此时参数savedInstanceState一定不为null，
    在onStart方法之后执行，如果在onRestoreInstanceState中读取savedInstanceState一定有值，如果在onCreate中读取Bundle存储的信息是有可能为null的。
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        LogUtils.getInstance().i("=============onRestoreInstanceState=================")
    }
    
}

@Composable
fun Greeting(name: String) {
    val mainActivity = LocalContext.current.getActivity<MainActivity>()
    Box(
        modifier = Modifier
            .background(Color.Green)
            .fillMaxHeight()
            .fillMaxWidth()
            .clickable {
                mainActivity?.let { MainActivity2.go(it) }
            },
        contentAlignment = Alignment.Center, // 居中对齐
//        propagateMinConstraints = true,
    ){
        Text(
            text = "$name!",
            fontSize = 50.sp,
            color = Color.Blue,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
//                .background(color = Color.White)
                .align(Alignment.Center),
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