package com.wl.mykotlintest.command

/**
Time:2020/7/6
Author:wl
Description:执行开关
 */
interface BindingFunction<T> {
    fun call():T
}