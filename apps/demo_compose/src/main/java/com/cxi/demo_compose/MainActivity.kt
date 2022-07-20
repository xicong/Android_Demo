package com.cxi.demo_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cxi.demo_compose.ui.theme.Android_DemoTheme
import com.cxi.demo_compose.ui.theme.NavigationBarColor
import com.cxi.demo_compose.ui.theme.StatusBarColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android_DemoTheme {
//                rememberSystemUiController().run {
//                    setStatusBarColor(StatusBarColor, false)
//                    setSystemBarsColor(StatusBarColor, false)
//                    setNavigationBarColor(NavigationBarColor, false)
//                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Spacer(Modifier.size(16.dp,16.dp))
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Android_DemoTheme {
        Greeting("Android")
    }
}