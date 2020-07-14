package com.wl.mykotlintest.utils.pic_selector

import com.luck.picture.lib.listener.OnResultCallbackListener

/**
Time:2020/7/14
Author:wl
Description:
 */
interface OnPictureSelectResult<T> : OnResultCallbackListener<T>{
    fun onSelectResult(list:List<String>)
    override fun onResult(result: MutableList<T>?) {
    }
    override fun onCancel() {
    }
}