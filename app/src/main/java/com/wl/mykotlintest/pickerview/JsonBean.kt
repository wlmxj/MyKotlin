package com.wl.mykotlintest.pickerview

import com.contrarywind.interfaces.IPickerViewData

/**
Time:2020/7/15
Author:wl
Description:
 */
class JsonBean: IPickerViewData {
    /**
     * name : 省份
     * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
     */
    private var name: String? = null
    private var city: List<CityBean?>? = null

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getCityList(): List<CityBean?>? {
        return city
    }

    fun setCityList(city: List<CityBean?>?) {
        this.city = city
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    override fun getPickerViewText(): String? {
        return name
    }


    class CityBean {
        /**
         * name : 城市
         * area : ["东城区","西城区","崇文区","昌平区"]
         */
        var name: String? = null
        var area: List<String>? = null

    }
}