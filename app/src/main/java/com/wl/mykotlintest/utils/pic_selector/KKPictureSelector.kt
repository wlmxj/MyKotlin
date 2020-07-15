package com.wl.mykotlintest.utils.pic_selector

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.camera.CustomCameraView
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.wl.mykotlintest.ac.MyImageSelector
import com.wl.mykotlintest.utils.GlideEngine
import java.lang.ref.WeakReference


/**
Time:2020/7/14
Author:wl
Description:
 */
class KKPictureSelector constructor(builder: Builder) {
    private var mBuilder: Builder? = null
    private var pSelector: PictureSelector? = null

    init {
        mBuilder = builder
    }

    fun create() {
        pSelector = when {
            mBuilder!!.mActivity != null -> {
                PictureSelector.create(mBuilder!!.mActivity)
            }
            else -> {
                PictureSelector.create(mBuilder!!.mFragment)
            }
        }
        pSelector?.let {
            if (mBuilder!!.mIsOpenCamera) {
                it.openCamera(getOpenType(mBuilder!!.openType))
            } else {
                it.openGallery(getOpenType(mBuilder!!.openType))
            }.setButtonFeatures(CustomCameraView.BUTTON_STATE_ONLY_CAPTURE)// 设置自定义相机按钮状态
                .withAspectRatio(1, 1) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .imageEngine(GlideEngine.createGlideEngine())
                .isCamera(mBuilder!!.mIsCamera)
                .selectionMode(getSelectMode(mBuilder!!.mSelectMode))
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: MutableList<LocalMedia?>?) {
                        var list = mutableListOf<String>()
                        for (media in result!!) {
                            Log.i(MyImageSelector.TAG, "是否压缩:" + media?.isCompressed)
                            Log.i(MyImageSelector.TAG, "压缩:" + media?.compressPath)
                            Log.i(MyImageSelector.TAG, "原图:" + media?.path)
                            Log.i(MyImageSelector.TAG, "是否裁剪:" + media?.isCut)
                            Log.i(MyImageSelector.TAG, "裁剪:" + media?.cutPath)
                            Log.i(MyImageSelector.TAG, "是否开启原图:" + media?.isOriginal)
                            Log.i(MyImageSelector.TAG, "原图路径:" + media?.originalPath)
                           val path = if (media!!.isCut && !media.isCompressed) {
                                // 裁剪过
                                media.cutPath
                            } else if (media.isCompressed || media.isCut && media.isCompressed) {
                                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                                media.compressPath
                            } else {
                                // 原图
                                media.path
                            }
                            list.add(path)
                        }
                        mBuilder!!.mCallBack!!.onSelectResult(list)
                    }

                    override fun onCancel() {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    private fun getOpenType(@MineType onpenType: Int): Int {
        return when (onpenType) {
            TYPE_ALL -> PictureMimeType.ofAll()
            TYPE_IMAGE -> PictureMimeType.ofImage()
            TYPE_VIDEO -> PictureMimeType.ofVideo()
            else -> PictureMimeType.ofAll()
        }
    }

    private fun getSelectMode(@SelectMode model: Int): Int {
        return when (model) {
            SINGLE -> PictureConfig.SINGLE
            MULTIPLE -> PictureConfig.MULTIPLE
            else -> PictureConfig.SINGLE
        }
    }


    class Builder() {
        internal var openType =
            TYPE_ALL
        internal var mIsOpenCamera = false
        internal var mIsCamera = false
        internal var mSelectMode = SINGLE
        internal var mCallBack: OnPictureSelectResult? = null
        internal var mActivity: Activity? = null
        internal var mFragment: Fragment? = null

        private constructor(activity: Activity?, fragment: Fragment?) : this() {
            mActivity = activity
            mFragment = fragment
        }

         constructor(activity: Activity):this(activity,null) {
//            Builder(activity, null)
        }

         constructor(fragment: Fragment) :this(null,fragment){
//            Builder(fragment.activity, fragment)
        }

        /**
         * 打开相册类型，图片；视频；图片加视频
         */
        fun openType(@MineType chooseMode: Int): Builder {
            openType = chooseMode
            return this
        }

        /**
         * 是否打开相机，true为相机 false为相册
         */
        fun openCamera(isOpen: Boolean): Builder {
            mIsOpenCamera = isOpen
            return this
        }

        /**
         * 相册是否带相机
         */
        fun isCamera(isCamera: Boolean): Builder {
            mIsCamera = isCamera
            return this
        }

        /**
         *相册选择模式 SINGLE：单选 MULTIPLE：多选 默认single
         */
        fun selectMode(@SelectMode mode: Int): Builder {
            mSelectMode = mode
            return this
        }

        /**
         * 回调接口
         */
        fun  forResult(callBack: OnPictureSelectResult): Builder {
            mCallBack = callBack
            return this
        }

        fun build(): KKPictureSelector {
            return KKPictureSelector(this)
        }
    }

}