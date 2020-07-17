package com.wl.mykotlintest.qrcode

/**
Time:2020/7/17
Author:wl
Description:
 */
interface KKScanReSsult {
    fun onScanQRCodeSuccess(result : String)
    fun onCameraAmbientBrightnessChanged(isDark:Boolean)
    fun onScanQRCodeOpenCameraError()
}