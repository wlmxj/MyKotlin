package com.wl.mykotlintest

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.wl.mykotlintest.command.BindingCommand
import com.wl.mykotlintest.testCode.TestClick
import java.util.concurrent.TimeUnit
import io.reactivex.functions.Consumer

/**
Time:2020/7/6
Author:wl
Description:
 */
object ViewAdapter {
    private const val CLICK_INTERVAL = 1.0.toLong()
    @BindingAdapter("wl:txt")
    @JvmStatic
    fun setTxt(view: TextView, msg:String){
        view.text = msg
    }

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     * isThrottleFirst 是否开启防止过快点击
     */
    @BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
    @JvmStatic
    fun onClickCommand(
        view: View?,
        clickCommand: BindingCommand<Boolean>?,
        isThrottleFirst: Boolean
    ) {
        Log.e("wl","onClickCommand")
        if (isThrottleFirst) {

            RxView.clicks(view!!).subscribe {
                clickCommand?.excute()
            }
        } else {

            RxView.clicks(view!!).throttleFirst(CLICK_INTERVAL,TimeUnit.SECONDS).subscribe {
                clickCommand?.excute()
            }
        }
    }
    @SuppressLint("CheckResult")
    @BindingAdapter("wl:clicktest")
    @JvmStatic
    fun onClickTest(view :View ,click:TestClick){
        Log.e("wl","onClickTest===${view.id}")
        RxView.clicks(view!!).throttleFirst(CLICK_INTERVAL,TimeUnit.SECONDS).subscribe {
            Log.e("wl","点击事件===${view.id}")
            click?.let {
                click.clickTest()
            }
        }
    }
}