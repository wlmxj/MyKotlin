package com.wl.mykotlintest.pickerview

import android.content.Context
import android.view.View

/**
Time:2020/7/15
Author:wl
Description:
 */
interface BaseStrategy{
    fun showTimerPickerView(context: Context, view: View, listener: KKOnTimeSelectListener)
}