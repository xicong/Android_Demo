package com.cong.demo.utils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cong.demo.R
import com.cxi.permission_lib.XcPermissionUtils

class UtilsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utils)
        XcPermissionUtils.locationPermission(this,ok = {
            Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show()
        },
        no = {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        )
    }
}