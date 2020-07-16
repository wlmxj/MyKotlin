package com.wl.mykotlintest.pickerview

import androidx.annotation.IntDef

/**
Time:2020/7/15
Author:wl
Description:
 */


@IntDef(PickerViewType.TYPE_TIMER, PickerViewType.TYPE_ADDRESS)
@Target(AnnotationTarget.FIELD,AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class PickerViewType{
    companion object{
        const val TYPE_TIMER = 0
        const val TYPE_ADDRESS = 1
    }
}