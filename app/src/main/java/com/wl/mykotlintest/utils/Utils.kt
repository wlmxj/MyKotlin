package com.wl.mykotlintest.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
Time:2020/7/15
Author:wl
Description:
 */
object Utils {
     fun getTime(date: Date): String? { //可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(date)
    }
}