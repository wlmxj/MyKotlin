package com.wl.mykotlintest.utils.pic_selector

import androidx.annotation.IntDef

/**
Time:2020/7/14
Author:wl
Description:选择图片类型
 */

const val TYPE_ALL = 0
const val TYPE_IMAGE = 1
const val TYPE_VIDEO = 2

@IntDef(TYPE_ALL, TYPE_IMAGE, TYPE_VIDEO)
@Target(AnnotationTarget.FIELD,AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class MineType