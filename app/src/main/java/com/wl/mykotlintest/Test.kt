package com.wl.mykotlintest

/**
Time:2020/7/7
Author:wl
Description:
 */

fun main() {
   val result = (2 > 3).yes {
        println("yes")
       false
    }.otherwise {
        println("no")
        false
    }
}