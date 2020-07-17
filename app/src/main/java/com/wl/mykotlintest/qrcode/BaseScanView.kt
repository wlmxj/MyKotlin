package com.wl.mykotlintest.qrcode

/**
Time:2020/7/17
Author:wl
Description:
 */
interface BaseScanView {
    fun addCallBack(result:KKScanReSsult)
    fun startScan()
    fun stopScan()
    fun openLight()
    fun closeLight()
}