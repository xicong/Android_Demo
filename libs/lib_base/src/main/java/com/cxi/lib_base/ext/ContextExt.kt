package com.cxi.lib_base.ext

import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import androidx.core.app.ComponentActivity
import com.cxi.lib_base.utils.BroadcastUtils

inline fun <reified Activity : ComponentActivity> Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        else -> {
            var context = this
            while (context is ContextWrapper) {
                context = context.baseContext
                if (context is Activity) return context
            }
            null
        }
    }
}

/**
 * 注册广播
 *
 * @param broadcastReceiver
 * @param action
 */
fun Context?.registerBroadcast(
    broadcastReceiver: BroadcastReceiver?,
    vararg action: String?
) = BroadcastUtils.getInstance().registerBroadcast(this, broadcastReceiver, *action)

/**
 * 解除注册广播，广播要和注册时是同一个
 *
 * @param broadcastReceiver
 */
fun Context?.unregisterBroadcast(broadcastReceiver: BroadcastReceiver?) =
    BroadcastUtils.getInstance().unregisterBroadcast(this, broadcastReceiver)