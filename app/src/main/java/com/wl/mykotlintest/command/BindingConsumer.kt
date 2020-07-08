package com.wl.mykotlintest.command

/**
Time:2020/7/6
Author:wl
Description:带参数的回调
 */
interface BindingConsumer<T> {
    fun  call(t:T)
}