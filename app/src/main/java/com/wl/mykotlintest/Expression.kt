package com.wl.mykotlintest

import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
Time:2020/7/6
Author:wl
Description:
 */
object Expression {
    @BindingAdapter("wl:txt")
    @JvmStatic
    fun setTxt(view: TextView, msg:String){
        view.text = msg
    }
}