package com.wl.mykotlintest.qrcode

import android.content.Context
import android.util.AttributeSet
import cn.bingoogolapple.qrcode.zxing.ZXingView

/**
Time:2020/7/17
Author:wl
Description:
 */
class KKScanView(context: Context,attributeSet: AttributeSet) :BaseScanView, ZXingView(context,attributeSet){

//    constructor(context: Context,attributeSet: AttributeSet,defStyleAttr:Int):this(context,attributeSet){
//    }
    private lateinit var scanResult:KKScanReSsult
    init {
        changeToScanQRCodeStyle()
        setDelegate(object : Delegate{
            override fun onScanQRCodeSuccess(result: String?) {
                result?.let { scanResult.onScanQRCodeSuccess(it) }
            }

            override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
                scanResult.onCameraAmbientBrightnessChanged(isDark)
            }

            override fun onScanQRCodeOpenCameraError() {
                scanResult.onScanQRCodeOpenCameraError()
            }

        })
    }
    override fun addCallBack(result: KKScanReSsult) {
        scanResult = result
    }

    override fun startScan() {
        startSpot()
    }

    override fun stopScan() {
        stopSpot()
    }

    override fun openLight() {
        openFlashlight()
    }

    override fun closeLight() {
        closeFlashlight()
    }
}