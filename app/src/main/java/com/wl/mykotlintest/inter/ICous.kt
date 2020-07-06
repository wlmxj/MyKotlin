package com.example.inter

import androidx.lifecycle.ViewModel
import java.lang.reflect.ParameterizedType

/**
Time:2020/7/2
Author:wl
Description:
 */
interface ICous<VM:ViewModel> {
    fun xxx():Class<VM>{
       return ((this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[1] as? Class<VM>)!!
    }
}