package com.wl.mykotlintest.pickerview

import android.content.Context
import android.view.View
import androidx.annotation.ColorInt
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import java.util.*

/**
Time:2020/7/15
Author:wl
Description:
 */
class KKTimePickerBuilder(context: Context, listener: OnTimeSelectListener) :
    TimePickerBuilder(context, listener) {

    fun addGravity(gravity: Int): KKTimePickerBuilder {
        setGravity(gravity)
        return this
    }


    override fun addOnCancelClickListener(cancelListener: View.OnClickListener): KKTimePickerBuilder {
        super.addOnCancelClickListener(cancelListener)
        return this
    }
    /**
     * //分别对应年月日时分秒，默认全部显示
     */
    override fun setType(type: BooleanArray?): KKTimePickerBuilder? {
        super.setType(type)
        return this
    }

    /**
     * 确认按钮文字
     */
    override fun setSubmitText(textContentConfirm: String?): KKTimePickerBuilder? {
       super.setSubmitText(textContentConfirm)
        return this
    }

    /**
     * 是否dialog形式
     */
    override fun isDialog(isDialog: Boolean): KKTimePickerBuilder? {
        super.isDialog(isDialog)
        return this
    }

    /**
     * 取消按钮文字
     */
    override fun setCancelText(textContentCancel: String?): KKTimePickerBuilder? {
       super.setCancelText(textContentCancel)
        return this
    }

    /**
     * 标题
     */
    override fun setTitleText(textContentTitle: String?): KKTimePickerBuilder? {
       super.setTitleText(textContentTitle)
        return this
    }

    /**
     * 确认按钮颜色
     */
    override fun setSubmitColor(textColorConfirm: Int): KKTimePickerBuilder? {
       super.setSubmitColor(textColorConfirm)
        return this
    }

    /**
     * 取消按钮颜色
     */
    override fun setCancelColor(textColorCancel: Int): KKTimePickerBuilder? {
       super.setCancelColor(textColorCancel)
        return this
    }

    /**
     * 滚轮背景颜色
     */
    override fun setBgColor(bgColorWheel: Int): KKTimePickerBuilder? {
        super.setBgColor(bgColorWheel)
        return this
    }

    /**
     * 标题背景颜色
     */
    override fun setTitleBgColor(bgColorTitle: Int): KKTimePickerBuilder? {
        super.setTitleBgColor(bgColorTitle)
        return this
    }

    /**
     * 标题文字颜色
     */
    override fun setTitleColor(textColorTitle: Int): KKTimePickerBuilder? {
        super.setTitleColor(textColorTitle)
        return this
    }

    /**
     * 确认按钮字号
     */
    override fun setSubCalSize(textSizeSubmitCancel: Int): KKTimePickerBuilder? {
        super.setSubCalSize(textSizeSubmitCancel)
        return this
    }

    /**
     * 设置标题字号
     */
    override fun setTitleSize(textSizeTitle: Int): KKTimePickerBuilder? {
       super.setTitleSize(textSizeTitle)
        return this
    }

    /**
     * 设置内容字号
     */
    override fun setContentTextSize(textSizeContent: Int): KKTimePickerBuilder? {
        super.setContentTextSize(textSizeContent)
        return this
    }

    /**
     * 设置最大可见数目
     *
     * @param count suggest value: 3, 5, 7, 9
     */
    override fun setItemVisibleCount(count: Int): KKTimePickerBuilder? {
        super.setItemVisibleCount(count)
        return this
    }

    /**
     * 透明度是否渐变
     *
     * @param isAlphaGradient true of false
     */
    override fun isAlphaGradient(isAlphaGradient: Boolean): KKTimePickerBuilder? {
        super.isAlphaGradient(isAlphaGradient)
        return this
    }

    /**
     * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
     *
     * @param date
     * @return TimePickerBuilder
     */
    override fun setDate(date: Calendar?): KKTimePickerBuilder? {
       super.setDate(date)
        return this
    }

    /**
     * 设置间距倍数,但是只能在1.0-4.0f之间
     *
     * @param lineSpacingMultiplier
     */
    override fun setLineSpacingMultiplier(lineSpacingMultiplier: Float): KKTimePickerBuilder? {
        super.setLineSpacingMultiplier(lineSpacingMultiplier)
        return this
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */
    override fun setDividerColor(@ColorInt dividerColor: Int): KKTimePickerBuilder? {
       super.setDividerColor(dividerColor)
        return this
    }

    /**
     * 设置分割线之间的文字的颜色---选中项的颜色值
     *
     * @param textColorCenter
     */
    override fun setTextColorCenter(@ColorInt textColorCenter: Int): KKTimePickerBuilder? {
        super.setTextColorCenter(textColorCenter)
        return this
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    override fun setTextColorOut(@ColorInt textColorOut: Int): KKTimePickerBuilder? {
        super.setTextColorOut(textColorOut)
        return this
    }

    /**
     * 是否循环
     */
    override fun isCyclic(cyclic: Boolean): KKTimePickerBuilder? {
        super.isCyclic(cyclic)
        return this
    }

     fun create(): KKTimePickerView {
        return KKTimePickerView().addView(super.build())
    }


}