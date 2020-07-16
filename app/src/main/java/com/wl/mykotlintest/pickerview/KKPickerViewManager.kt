package com.wl.mykotlintest.pickerview

import android.content.Context
import android.view.View


/**
Time:2020/7/15
Author:wl
Description:
 */
object KKPickerViewManager{
    fun showPickerView(@PickerViewType type:Int,context:Context,view:View,listener: KKOnTimeSelectListener){
        when(type){
           PickerViewType.TYPE_TIMER -> showTimerPicker(context,view,listener)
            PickerViewType.TYPE_ADDRESS -> showAddressPicker(context)
        }
    }

    private fun showAddressPicker(context: Context) {
        var picker = KKWindPickerViewStategy()
        picker.showAddressPickerView(context)
    }

    private fun showTimerPicker(context:Context,view:View,listener: KKOnTimeSelectListener) {
        var picker = KKWindPickerViewStategy()
        picker.showTimerPickerView(context,view,listener)
    }
}