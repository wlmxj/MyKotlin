package com.wl.mykotlintest.ac

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.base.BaseActivity
import com.wl.mykotlintest.R
import com.wl.mykotlintest.databinding.ActivityQrCodeBinding
import com.wl.mykotlintest.qrcode.KKScanReSsult
import com.wl.mykotlintest.vm.UserViewModel
import kotlinx.android.synthetic.main.activity_qr_code.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
Time:2020/7/16
Author:wl
Description:
 */
class QRCodeActivity:BaseActivity<ActivityQrCodeBinding,UserViewModel>() {
    override fun initData() {
    }

    override fun initView() {
        zxingview.addCallBack(object : KKScanReSsult{
            override fun onScanQRCodeSuccess(result: String) {
                LogUtils.d("二维码扫描结果result:$result")
                ToastUtils.showShort(result)

                //扫描得到结果震动一下表示

//                zxingview.startSpot()
                GlobalScope.launch {
                    delay(200)
                    finish()

                }
            }

            override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {

            }

            override fun onScanQRCodeOpenCameraError() {
                LogUtils.d("打开相机错误！")
            }

        })

        zxingview.startSpot()

        start_spot.setOnClickListener {
            zxingview.startSpot()
        }

        close_flashlight.setOnClickListener {
            zxingview.stopSpot()
        }
        open_flashlight.setOnClickListener {
            zxingview.openFlashlight()
        }
        close_flashlight.setOnClickListener {
            zxingview.closeFlashlight()
        }
    }

    //震动
    private fun vibrate() {
        val vibrator =
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_qr_code
    }

}