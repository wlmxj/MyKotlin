package com.wl.mykotlintest.pickerview

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.wl.mykotlintest.R
import com.wl.mykotlintest.utils.GetJsonDataUtil
import com.wl.mykotlintest.utils.Utils
import kotlinx.coroutines.*
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

/**
Time:2020/7/15
Author:wl
Description:
 */
class KKWindPickerViewStategy : BaseStrategy {
    private var pvTime: KKTimePickerView? = null
    private var selectData: Date? = null
    private var calendar = Calendar.getInstance()
    override fun showTimerPickerView(
        context: Context,
        view: View,
        listener: KKOnTimeSelectListener
    ) {
        pvTime = KKTimePickerBuilder(context,
            OnTimeSelectListener { date, _ ->
                Toast.makeText(context, Utils.getTime(date), Toast.LENGTH_SHORT).show()
                selectData = date
                calendar.time = this.selectData
                listener.onSelet(selectData, view)
            }).apply {
            setPickerViewParams(listener, context)
        }.create()

        initDialog()
        pvTime!!.show(view) //弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
    }

    private fun KKTimePickerBuilder.setPickerViewParams(
        listener: KKOnTimeSelectListener,
        context: Context
    ) {
        setSubmitColor(Color.RED)//确定按钮文字颜色
        setCancelColor(Color.RED)//取消按钮文字颜色
        setTimeSelectChangeListener {
            listener.onTimeSelectChanged(it)
            Log.i("pvTime", "onTimeSelectChanged")
        }
        setType(booleanArrayOf(true, true, true, false, false, false))
        isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
        addOnCancelClickListener { Log.i("pvTime", "onCancelClickListener") }
        setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
        setLineSpacingMultiplier(2.0f)
        setDate(calendar)
        setOutSideColor(0xFF0080)
        setTextColorCenter(context.resources.getColor(R.color.app_color_red))
        isAlphaGradient(true)
    }

    private fun initDialog() {
        pvTime?.mView?.dialog.let {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )
            params.leftMargin = 0
            params.rightMargin = 0
            pvTime?.mView?.dialogContainerLayout?.layoutParams = params
            val dialogWindow = it?.window
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM) //改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f)
            }
        }
    }

    private var options1Items = mutableListOf<JsonBean>()
    private val options2Items = mutableListOf<ArrayList<String>>()
    private val options3Items = mutableListOf<ArrayList<ArrayList<String>>>()
    fun showAddressPickerView(context: Context) {
        val job = GlobalScope.launch(Dispatchers.Main) {

            val def = async(Dispatchers.IO) {
                val jsonData =
                    GetJsonDataUtil.getJson(context, "province.json")//获取assets目录下的json文件数据
                val jsonBean: ArrayList<JsonBean>? = parseData(jsonData) //用Gson 转成实体
                options1Items = jsonBean!!
                for (i in 0 until jsonBean!!.size) {
                    //遍历省份
                    var cityList = ArrayList<String>()//该省的城市列表（第二级）
                    var provinceArealist =
                        ArrayList<ArrayList<String>>() //该省的所有地区列表（第三极）
                    for (c in jsonBean[i].getCityList()!!.indices) { //遍历该省份的所有城市
                        val cityName: String = jsonBean[i].getCityList()!![c]?.name!!
                        cityList.add(cityName) //添加城市
                        val cityArealist =
                            ArrayList<String>() //该城市的所有地区列表

                        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                        /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                        cityArealist.addAll(jsonBean[i].getCityList()!![c]?.area!!)
                        provinceArealist.add(cityArealist) //添加该省所有地区数据
                    }
                    options2Items.add(cityList)//添加城市数据
                    options3Items.add(provinceArealist)//添加地区数据
                }
                withContext(Dispatchers.Main) {
                    showPickerView(context)
                }

            }

        }
    }

    fun parseData(result: String?): ArrayList<JsonBean>? { //Gson 解析
        val detail: ArrayList<JsonBean> = ArrayList<JsonBean>()
        try {
            val data = JSONArray(result)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity: JsonBean =
                    gson.fromJson(data.optJSONObject(i).toString(), JsonBean::class.java)
                detail.add(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.e("异常：${e.message}")
        }
        return detail
    }

    private fun showPickerView(context: Context) { // 弹出选择器
        val pvOptions: OptionsPickerView<Any> =
            OptionsPickerBuilder(context,
                OnOptionsSelectListener { options1, options2, options3, v -> //返回的分别是三个级别的选中位置
                    val opt1tx =
                        if (options1Items.size > 0) options1Items[options1]
                            .pickerViewText else ""
                    val opt2tx = if (options2Items.size > 0
                        && options2Items[options1].size > 0
                    ) options2Items[options1][options2] else ""
                    val opt3tx =
                        if (options2Items.size > 0 && options3Items[options1].size > 0 && options3Items[options1][options2].size > 0
                        ) options3Items[options1][options2][options3] else ""
                    val tx = opt1tx + opt2tx + opt3tx
                    Toast.makeText(context, tx, Toast.LENGTH_SHORT).show()
                })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build()

//        pvOptions.setPicker(options1Items as List<Any>?)//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(
            options1Items as List<Any>?,
            options2Items as List<MutableList<Any>>?,
            options3Items as List<MutableList<MutableList<Any>>>?
        ) //三级选择器
        pvOptions.show()
    }

}