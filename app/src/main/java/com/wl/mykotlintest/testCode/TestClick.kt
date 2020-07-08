package com.wl.mykotlintest.testCode

import android.util.Log

/**
Time:2020/7/8
Author:wl
Description:
 */
class TestClick() {
     private var comand:CommmandClick ?= null
     constructor(conmand:CommmandClick) : this(){
         this.comand = conmand
     }
    fun clickTest(){
        Log.e("wl","clickTest")
        comand?.clickAction()
    }
    interface CommmandClick{
        fun clickAction()
    }
}