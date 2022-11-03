package com.cxi.demo_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.cxi.lib_base.ext.getActivity

class MainActivity2 : ComponentActivity() {
    
    companion object{
        fun go(activity:Activity){
            activity.startActivity(Intent(activity,MainActivity2::class.java))
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainActivity = LocalContext.current.getActivity<MainActivity2>()
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .background(Color.Red)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable {
                        mainActivity?.finish()
                    }
            ){
                Text(
                    text = "2!",    
                    fontSize = 50.sp
                )
            }
        }
    }
    
}