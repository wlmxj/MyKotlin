package com.wl.mykotlintest.utils.pic_selector

import androidx.annotation.IntDef

/**
Time:2020/7/14
Author:wl
Description:
 */
const val SINGLE = 1
const val MULTIPLE = 2
@IntDef(SINGLE, MULTIPLE)
annotation class SelectMode