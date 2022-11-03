package com.cxi.lib_base.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

class BroadcastUtils private constructor() {

    companion object {
        fun getInstance() = Helper.instance
    }

    private object Helper {
        val instance = BroadcastUtils()
    }

    /**
     * 注册广播
     *
     * @param context
     * @param broadcastReceiver
     * @param action
     */
    fun registerBroadcast(
        context: Context?,
        broadcastReceiver: BroadcastReceiver?,
        vararg action: String?
    ) {
        if (context == null || broadcastReceiver == null) return
        val intentFilter = IntentFilter()
        for (element in action) {
            intentFilter.addAction(element)
        }
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    /**
     * 解除注册广播，广播要和注册时是同一个
     *
     * @param context
     * @param broadcastReceiver
     */
    fun unregisterBroadcast(context: Context?, broadcastReceiver: BroadcastReceiver?) {
        if (context == null || broadcastReceiver == null) return
        context.unregisterReceiver(broadcastReceiver)
    }
    
}