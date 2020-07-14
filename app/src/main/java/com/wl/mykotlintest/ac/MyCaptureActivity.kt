package com.wl.mykotlintest.ac

import android.os.Bundle
import com.king.zxing.CaptureActivity
import com.wl.mykotlintest.R

/**
Time:2020/7/10
Author:wl
Description:
 */
class MyCaptureActivity : CaptureActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_capture)
    }
}