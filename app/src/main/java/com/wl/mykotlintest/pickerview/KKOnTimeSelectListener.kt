package com.wl.mykotlintest.pickerview

import android.view.View
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import java.util.*

/**
Time:2020/7/15
Author:wl
Description:
 */
interface KKOnTimeSelectListener{
    fun onSelet(date: Date?, v: View?)
    fun onTimeSelectChanged(data:Date){
    }
}