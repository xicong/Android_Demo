# 学习Activity的启动过程的笔记

https://zhuanlan.zhihu.com/p/67451239

## 相关主类
* Instrumentation：Instrumentation会在应用程序的任何代码运行之前被实例化,它能够允许你监视应用程序和系统的所有交互.它还会构造Application,构建Activity,以及生命周期都会经过这个对象去执行.
* ActivityManagerService：Android核心服务,简称AMS,负责调度各应用进程,管理四大组件.实现了IActivityManager接口,应用进程能通过Binder机制调用系统服务.
* LaunchActivityItem：相当于是一个消息对象,当ActivityThread接收到这个消息则去启动Activity.收到消息后执行execute方法启动activity.
* ActivityThread：应用的主线程.程序的入口.在main方法中开启loop循环,不断地接收消息,处理任务.

