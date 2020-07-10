package com.wl.mykotlintest.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.blankj.utilcode.util.LogUtils
import java.io.FileInputStream
import java.io.FileNotFoundException


/**
Time:2020/7/10
Author:wl
Description:
 */
object PathUtils {
    fun getLoacalBitmap(path:String):Bitmap?{
        return try {
            val fis = FileInputStream(path)
            BitmapFactory.decodeStream(fis) ///把流转化为Bitmap图片
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            LogUtils.e("exception:"+e.message)
            null
        }
    }
}