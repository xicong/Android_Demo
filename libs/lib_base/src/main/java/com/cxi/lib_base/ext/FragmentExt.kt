package com.cxi.lib_base.ext

import android.app.Fragment
import android.content.BroadcastReceiver
import com.cxi.lib_base.utils.BroadcastUtils

/**
 * 注册广播
 *
 * @param broadcastReceiver
 * @param action
 */
fun Fragment?.registerBroadcast(
    broadcastReceiver: BroadcastReceiver?,
    vararg action: String?
) = BroadcastUtils.getInstance().registerBroadcast(this?.context, broadcastReceiver, *action)

/**
 * 解除注册广播，广播要和注册时是同一个
 *
 * @param broadcastReceiver
 */
fun Fragment?.unregisterBroadcast(broadcastReceiver: BroadcastReceiver?) =
    BroadcastUtils.getInstance().unregisterBroadcast(this?.context, broadcastReceiver)