package com.wl.mykotlintest.ac

import android.app.Dialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.wl.mykotlintest.R
import com.wl.mykotlintest.pickerview.KKOnTimeSelectListener
import com.wl.mykotlintest.pickerview.KKPickerViewManager
import com.wl.mykotlintest.pickerview.PickerViewType
import com.wl.mykotlintest.utils.Utils
import com.wl.mykotlintest.utils.pic_selector.KKPictureSelector
import com.wl.mykotlintest.utils.pic_selector.MULTIPLE
import com.wl.mykotlintest.utils.pic_selector.OnPictureSelectResult
import com.wl.mykotlintest.utils.pic_selector.TYPE_IMAGE
import kotlinx.android.synthetic.main.ac_layout_simple_picture.*
import java.text.SimpleDateFormat
import java.util.*


/**
Time:2020/7/14
Author:wl
Description:
 */
class SimplePictuerActivity : AppCompatActivity() {
    var path: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_layout_simple_picture)
        bt_pic.setOnClickListener {
//            PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())
////                .setButtonFeatures(CustomCameraView.BUTTON_STATE_ONLY_CAPTURE)// 设置自定义相机按钮状态
////                .loadImageEngine(GlideEngine.createGlideEngine())
//                .withAspectRatio(1, 1) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .imageEngine(GlideEngine.createGlideEngine())
//                .isCamera(true)
//                .selectionMode(PictureConfig.SINGLE)
//                .forResult(object : OnResultCallbackListener<LocalMedia?> {
//                    override fun onResult(result: List<LocalMedia?>) {
//                        // onResult Callback
//                        for (media in result) {
//                            Log.i(MyImageSelector.TAG, "是否压缩:" + media?.isCompressed)
//                            Log.i(MyImageSelector.TAG, "压缩:" + media?.compressPath)
//                            Log.i(MyImageSelector.TAG, "原图:" + media?.path)
//                            Log.i(MyImageSelector.TAG, "是否裁剪:" + media?.isCut)
//                            Log.i(MyImageSelector.TAG, "裁剪:" + media?.cutPath)
//                            Log.i(MyImageSelector.TAG, "是否开启原图:" + media?.isOriginal)
//                            Log.i(MyImageSelector.TAG, "原图路径:" + media?.originalPath)
//                            Log.i(
//                                MyImageSelector.TAG,
//                                "Android Q 特有Path:" + media?.androidQToPath
//                            )
//                            Log.i(
//                                MyImageSelector.TAG,
//                                "宽高: " + media?.width + "x" + media?.height
//                            )
//                            Log.i(MyImageSelector.TAG, "Size: " + media?.size)
//                            // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
//
//                            loadImage(media)
//                        }
//                    }
//
//                    override fun onCancel() {
//                        // onCancel Callback
//                    }
//                })

            KKPictureSelector.Builder(this).openType(TYPE_IMAGE).isCamera(true).selectMode(MULTIPLE).openCamera(false).forResult(
                callBack = object:OnPictureSelectResult{
                    override fun onSelectResult(list: List<String>) {
                        LogUtils.e("result.size===${list.size}")
                        Glide.with(this@SimplePictuerActivity).load(
                            list[0]
                        ).into(iv_selected)
                    }

                }).build().create()
        }
        bt_camera.setOnClickListener {
//            PictureSelector.create(this)
//                .openCamera(PictureMimeType.ofImage())
//                .imageEngine(GlideEngine.createGlideEngine())
//                .withAspectRatio(1, 1) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .forResult(object : OnResultCallbackListener<LocalMedia?> {
//                    override fun onResult(result: List<LocalMedia?>) {
//                        // onResult Callback
//                        for (media in result) {
//                            Log.i(MyImageSelector.TAG, "是否压缩:" + media?.isCompressed)
//                            Log.i(MyImageSelector.TAG, "压缩:" + media?.compressPath)
//                            Log.i(MyImageSelector.TAG, "原图:" + media?.path)
//                            Log.i(MyImageSelector.TAG, "是否裁剪:" + media?.isCut)
//                            Log.i(MyImageSelector.TAG, "裁剪:" + media?.cutPath)
//                            Log.i(MyImageSelector.TAG, "是否开启原图:" + media?.isOriginal)
//                            Log.i(MyImageSelector.TAG, "原图路径:" + media?.originalPath)
//                            Log.i(
//                                MyImageSelector.TAG,
//                                "Android Q 特有Path:" + media?.androidQToPath
//                            )
//                            Log.i(
//                                MyImageSelector.TAG,
//                                "宽高: " + media?.width + "x" + media?.height
//                            )
//                            Log.i(MyImageSelector.TAG, "Size: " + media?.size)
//                            // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
//                            loadImage(media)
//                        }
//                    }
//
//                    override fun onCancel() {
//                        // onCancel Callback
//                    }
//                })

            KKPictureSelector.Builder(this).openType(TYPE_IMAGE).openCamera(true).forResult(object:OnPictureSelectResult{
                override fun onSelectResult(list: List<String>) {
                   LogUtils.e("result.size===${list.size}")
                    Glide.with(this@SimplePictuerActivity).load(
                        list[0]
                    ).into(iv_selected)
                }

            }).build().create()
        }

        bt_timer_picker.setOnClickListener {
//            initTimePicker(bt_timer_picker)
//            KKPickerViewManager.showTimerPickerView(this@SimplePictuerActivity,bt_timer_picker,object :KKOnTimeSelectListener{
//                override fun onSelet(date: Date?, v: View?) {
//                    LogUtils.d("结果：${date?.let { it1 -> Utils.getTime(it1) }}")
//                }
//
//            })
            KKPickerViewManager.showPickerView(PickerViewType.TYPE_ADDRESS,this,bt_timer_picker,object :KKOnTimeSelectListener{
                override fun onSelet(date: Date?, v: View?) {
                    LogUtils.e(date?.let { it1 -> Utils.getTime(it1) })
                }

            })
        }
    }

    //https://www.jianshu.com/p/23df7a40e511  https://github.com/Bigkoo/Android-PickerView
    private var pvTime : TimePickerView ?= null
    private var selectData:Date ?= null
    var calendar = Calendar.getInstance()

    private fun initTimePicker(view:View) { //Dialog 模式下，在底部弹出
        pvTime = TimePickerBuilder(this,
            OnTimeSelectListener { date, _ ->
                Toast.makeText(this@SimplePictuerActivity, getTime(date), Toast.LENGTH_SHORT).show()
                Log.i("pvTime", "onTimeSelect")
                selectData = date
                calendar.time = selectData
            })
            .setSubmitColor(Color.RED)//确定按钮文字颜色
            .setCancelColor(Color.RED)//取消按钮文字颜色
            .setTimeSelectChangeListener { Log.i("pvTime", "onTimeSelectChanged") }
            .setType(booleanArrayOf(true, true, true,false,false,false))
            .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
            .addOnCancelClickListener { Log.i("pvTime", "onCancelClickListener") }
            .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
            .setLineSpacingMultiplier(2.0f)
            .setDate(calendar)
            .setOutSideColor(0xFF0080)
            .setTextColorCenter(resources.getColor(R.color.app_color_red))
            .isAlphaGradient(true)
            .build()
        val mDialog: Dialog = pvTime?.dialog!!
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )
            params.leftMargin = 0
            params.rightMargin = 0
            pvTime?.dialogContainerLayout?.layoutParams = params
            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM) //改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f)
            }
        }
        // pvTime.setDate(Calendar.getInstance());
        /* pvTime.show(); //show timePicker*/
        pvTime!!.show(view) //弹出时间选择器，传递参数过去，回调的时候则可以绑定此view

    }

    private fun getTime(date: Date): String? { //可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(date)
    }
    private fun loadImage(media: LocalMedia?) {
        path = if (media!!.isCut && !media.isCompressed) {
            // 裁剪过
            media.cutPath
        } else if (media.isCompressed || media.isCut && media.isCompressed) {
            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            media.compressPath
        } else {
            // 原图
            media.path
        }
        Glide.with(this@SimplePictuerActivity).load(
            if (PictureMimeType.isContent(
                    path
                ) && !media!!.isCut && !media!!.isCompressed
            ) Uri.parse(path) else path
        ).into(iv_selected)
    }




}