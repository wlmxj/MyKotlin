package com.wl.mykotlintest.ac

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.camera.CustomCameraView
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.wl.mykotlintest.R
import com.wl.mykotlintest.utils.GlideEngine
import com.wl.mykotlintest.utils.pic_selector.*
import kotlinx.android.synthetic.main.ac_layout_simple_picture.*


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

            KKPictureSelector.Builder(this).openType(TYPE_IMAGE).isCamera(true).selectMode(MULTIPLE).openCamera(false).forResult(object:OnPictureSelectResult<LocalMedia>{
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

            KKPictureSelector.Builder(this).openType(TYPE_IMAGE).openCamera(true).forResult(object:OnPictureSelectResult<LocalMedia>{
                override fun onSelectResult(list: List<String>) {
                   LogUtils.e("result.size===${list.size}")
                    Glide.with(this@SimplePictuerActivity).load(
                        list[0]
                    ).into(iv_selected)
                }

            }).build().create()
        }
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