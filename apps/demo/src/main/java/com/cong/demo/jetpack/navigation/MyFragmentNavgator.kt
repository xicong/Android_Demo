package com.cong.demo.jetpack.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.*

/**
 * 目前在tab+page 存在问题  显示会重叠  不该销毁的时候会被销毁   返回要关闭activity也就是多个fragment
 */
@Navigator.Name("fragment")
class MyFragmentNavgator(
    val mContext:Context,
    val mFragmentManager:FragmentManager,
    val mContainerId:Int
) : FragmentNavigator(mContext, mFragmentManager, mContainerId) {
    
    val TAG = "MyFragmentNavgator"

    @ExperimentalStdlibApi
    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (mFragmentManager.isStateSaved) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
//        val frag = instantiateFragment(
//            mContext, mFragmentManager,
//            className, args
//        )
//        frag.arguments = args
        val ft = mFragmentManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        //处理fragment切换的事务
        val tag = destination.id.toString()
        var frag = mFragmentManager.findFragmentByTag(tag)
        if(frag == null){
            frag = instantiateFragment(
                mContext, mFragmentManager,
                className, args
            )
            frag.arguments = args
            ft.add(mContainerId,frag,tag)
        }else{
            ft.show(frag)
        }
//        ft.replace(mContainerId, frag)
        ft.setPrimaryNavigationFragment(frag)
        
        //用反射获取私有变量
        var mBackStack : java.util.ArrayDeque<Int>? = null
        try {
            val declaredField= FragmentNavigator::class.java.getDeclaredField("mBackStack")
            declaredField.isAccessible = true
            mBackStack = declaredField.get(this) as ArrayDeque<Int>?
        }catch (e:NoSuchFieldException){
            e.printStackTrace()
        }catch (e:SecurityException){
            e.printStackTrace()
        }catch (e:IllegalArgumentException){
            e.printStackTrace()
        }catch (e:IllegalAccessException){
            e.printStackTrace()
        }
        
        @IdRes val destId = destination.id
        val initialNavigation = mBackStack?.isEmpty()
        // TODO Build first class singleTop behavior for fragments
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (navOptions != null && initialNavigation!!
                && navOptions.shouldLaunchSingleTop()
                && mBackStack?.peekLast() == destId)

        val isAdded: Boolean
        isAdded = if (initialNavigation!!) {
            true
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack!!.size > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                mFragmentManager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack?.size!! + 1, destId))
            true
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key!!, value!!)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        // The commit succeeded, update our view of the world
        return if (isAdded) {
            mBackStack?.add(destId)
            destination
        } else {
            null
        }
//        return super.navigate(destination, args, navOptions, navigatorExtras)
    }

    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }
    
}


