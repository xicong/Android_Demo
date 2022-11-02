package com.cxi.demo_activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class MainActivity2 : AppCompatActivity() {
    
    companion object{
        fun go(activity:Activity){
            activity.startActivity(Intent(activity,MainActivity2::class.java))
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .background(Color.Red)
            ){
                Text(
                    text = "2!",
                    modifier = Modifier.clickable {

                    }
                )
            }
        }
    }
    
}