package com.cong.demo.xiancheng

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.cong.demo.BaseActivity
import com.cong.demo.R
import kotlinx.android.synthetic.main.layout_activity_xiecheng_kotlin.*
import kotlinx.coroutines.*

/**
 * java中的线程
 * 
 * 
 * kotlin中的协程  一个线程框架  方便在可以在同一个代码块进行多次的线程切换
 * 
 * 
 * 协程的创建方式
 * runBlocking  创建新的协程，运行在当前线程上，所以会阻塞当前线程，直到协程体结束，但是这个runBlocking域中可以有多个携程，多个协程可以并发进行，不用等待子协程执行结束
 *                        用于启动一个协程，通常只用于启动最外层的协程，例如线程环境切换到协程环境
 * launch   启动一个新的线程，在新线程上创建运行协程，不阻塞当前线程
 *               需要启动异步线程处理的情况
 * CoroutineScope  在指定类型的线程中创建协程，不会阻塞所运行的线程  可以理解为协程的作用域，可以管理作用域内的所有协程
 * 
 * 
 * 上面的launch方法可以的并列方法是async方法
 *      lauch：协程构建器，创建并启动(也可以延时启动)一个协程，返回一个Job，用于监督和取消任务，用于无返回值的场景。
 *      async：协程构建器，和launch一样，区别是返回一个Job的子类 Deferred，async可以在协程体中自定义返回值，并且通过Deferred.await堵塞当前线程等待接收async协程返回的类型。特别是需要启动异步线程处理并等待处理结果返回的场景
 * CoroutineScope 创建新一个子域，并管理域中的所有协程。注意这个方法只有在block中创建的所有子协程全部执行完毕后，才会退出。
 * SuperVisorScope 在子协程失败时，错误不会往上传递给父域，所以不会影响子协程     
 *
 * 
 * suspend  协程关键字 用来标识一个函数是挂起函数  非阻塞式挂起  从当前线程挂起
 * resume  恢复挂起
 * 
 * withCpntext()   自定义挂起函数    方便线程切换
 * delay 自定义挂起函数   等待后执行
 * 
 * 
 * 非阻塞式挂起
 * 协程只是用看起来阻塞的方式写出来的非阻塞式的效果
 * 
 * 协程默认的四种调度方式  通过withcontext可以指定
 *  Dispatcher.Default     使用共享线程运行协程      cpu密集型任务   解析json文件  排序较大的list
 *  Dispatcher.Main  ui线程      更新ui
 *  Dispatcher.Io  io线程     用于网络请求和文件访问  数据库操作
 *  Dispatcher.Unconfined   使用父协程运行的线程     高级调度器，不应该在常规的代码中使用
 *  newSingleThreadContext   在新线程中运行协程
 */
class Activity_Kotlin_XieCheng : BaseActivity(){
    
    var num = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_xiecheng_kotlin)

//        xiechengrunBlocking()
//        xiechenglaunch()
        xiechengCoroutineScope()

        tv_activity_xiecheng_kotlin_kaishixiancheng.setOnClickListener { 
//            xiancheng()
        }
    }

    /**
     * 因为协程本身轻量级，但是做的事情都比较重，比如读写文件或者网络请求使用代码手动跟踪大量的协程是相当困难的，这样的代码容易出错，一旦对协程失去追踪，那么就会导致泄露，这比内存泄露更严重，因为失去追踪的协程在resume的时候可能会消耗内存，cpu，磁盘，甚至会进行不再必要的网络请求
     * 所以CoroutineScope就出来了，通过launch，async启动一个协程需要指定CoroutineScope，取消只需要CoroutineScope.cancel,kotlin会帮我们自动取消在这个作用域内启动的协程
     * 
     * 
     */
    fun xiechengCoroutineScope(){
        val viewModleJob = Job()    //用来取消协程
         val uiscope = CoroutineScope(Dispatchers.Main + viewModleJob) //初始化CoroutineScope指定协程的运行所在线程传入Job方便后面取消协程
         uiscope.launch { //启动一个协程 
             updateUi()
         }
         uiscope.async { //启动一个带返回结果的协程   Deferred.await 获取结果  有异常不会直接抛出  只会在调用await的时候抛出
         }
//         viewModleJob.cancel()   //取消协程
         //withContext启动一个协程  coroutineContext改变协程运行的上下文
    }

     suspend  fun updateUi() {
       delay(1000)
         LogUtils.i("=============00================")
    }

    /**
     * 一个不阻塞线程的携程例子
     */
    fun xiechenglaunch(){
        LogUtils.i("=============00================")
        GlobalScope.launch {
            LogUtils.i("=============11================")
        }
        LogUtils.i("=============22================")
    }

    /**
     * 一个可以阻塞线程的例子
     */
    fun xiechengrunBlocking(){
        LogUtils.i("=============00================")
        runBlocking {
            LogUtils.i("=============11================")
        }
        LogUtils.i("=============22================")
    }
    
    /**
     * 一般java中获取线程的返回值要通过callback
     */
    fun xiancheng(){
        LogUtils.i("=======协程代码之前===========")
        //协程的创建方式launch
        //还有一个方式async
        var xc = GlobalScope.launch {
            //后台耗时操作
            LogUtils.i("=======开始计数===========")
            for (i in 0..10000){
                delay(1000)
                ++num
            }
            LogUtils.i("=======计数结束===========")
        }
        LogUtils.i("=======协程代码之后===========")
//        xc.cancel()  //线程退出
    }


//    tv_activity_xiecheng_kotlin_num.text = "${i}"
    
}