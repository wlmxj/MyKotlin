package com.wl.mykotlintest.pickerview

import android.view.View
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.LogUtils

/**
Time:2020/7/15
Author:wl
Description:
 */
class KKTimePickerView{
    var mView:TimePickerView? = null
    fun addView(view: TimePickerView) : KKTimePickerView{
        mView = view
        return this
    }

    fun show(view:View){
        mView?.let { it.show(view) }
    }

}