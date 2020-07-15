package com.wl.mykotlintest.pickerview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.wl.mykotlintest.R
import com.wl.mykotlintest.utils.Utils
import java.util.*

/**
Time:2020/7/15
Author:wl
Description:
 */
object KKPickerViewManager {
    private var pvTime: TimePickerView? = null
    private var selectData: Date? = null
    private var calendar = Calendar.getInstance()
    fun showPickerView(context: Context, view: View, listener: KKOnTimeSelectListener) {
        pvTime = KKTimePickerBuilder(context,
            OnTimeSelectListener { date, _ ->
                Toast.makeText(context, Utils.getTime(date), Toast.LENGTH_SHORT).show()
                selectData = date
                calendar.time = this.selectData
                listener.onSelet(selectData, view)
            }).apply {
            setPickerViewParams(listener, context)
        }.build()

        initDialog()
        pvTime!!.show(view) //弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
    }

    private fun initDialog() {
        pvTime?.dialog.let {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )
            params.leftMargin = 0
            params.rightMargin = 0
            pvTime?.dialogContainerLayout?.layoutParams = params
            val dialogWindow = it?.window
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM) //改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f)
            }
        }
    }

    private fun KKTimePickerBuilder.setPickerViewParams(
        listener: KKOnTimeSelectListener,
        context: Context
    ) {
        setSubmitColor(Color.RED)//确定按钮文字颜色
        setCancelColor(Color.RED)//取消按钮文字颜色
        setTimeSelectChangeListener {
            listener.onTimeSelectChanged(it)
            Log.i("pvTime", "onTimeSelectChanged")
        }
        setType(booleanArrayOf(true, true, true, false, false, false))
        isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
        addOnCancelClickListener { Log.i("pvTime", "onCancelClickListener") }
        setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
        setLineSpacingMultiplier(2.0f)
        setDate(calendar)
        setOutSideColor(0xFF0080)
        setTextColorCenter(context.resources.getColor(R.color.app_color_red))
        isAlphaGradient(true)
    }
}